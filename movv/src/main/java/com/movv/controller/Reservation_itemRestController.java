package com.movv.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.movv.entity.Reservation;
import com.movv.entity.Reservation_item;
import com.movv.repository.Reservation_itemRepository;

@RestController
public class Reservation_itemRestController {
	@Autowired
	private Reservation_itemRepository repository;
	
	// 예약 아이템 확인 (Map 사용한 방법)
//	@PostMapping("/checkReserved_item")
//	public List<Map<String, Object>> checkReserved_item(@RequestBody Map<String, String> request) {
//		
//		String status=request.get("status");
//		List<Reservation_item> list_raw= repository.findReservation_itemByStatus(status);
//		List<Map<String, Object>> list=new ArrayList<>();
//	    for (Reservation_item item : list_raw) {
//	        Map<String, Object> map = new HashMap<>();
//	        map.put("rno", item.getRno());
//	        map.put("ino", item.getIno());
//	        map.put("status", item.getStatus());
//	        map.put("price", item.getPrice());
//	        map.put("title", item.getTitle());
//	        map.put("cancel_dt", item.getCancel_dt());
//	        map.put("reg_dt", item.getReg_dt());
//	        // 여기에 다른 필드를 추가하세요
//
//	        list.add(map);
//	    }
//	    System.out.println(list);
//		return list;
//	}
	
	// 예약 아이템 확인 (DTO 사용한 방법)
    @PostMapping("/checkReserved_item")
    public ResponseEntity<List<Reservation_item>> checkReserved_item(@RequestBody Map<String, String> request) {
        String status = request.get("status");
        List<Reservation_item> reservationItems = repository.findReservation_itemByStatus(status);

        List<Reservation_item> reservationItemDTOs = reservationItems.stream().map(item -> {
        	Reservation_item dto = new Reservation_item();
            dto.setRno(item.getRno());
            dto.setIno(item.getIno());
            dto.setStatus(item.getStatus());
            dto.setPrice(item.getPrice());
            dto.setTitle(item.getTitle());
            dto.setCancel_dt(item.getCancel_dt());
            dto.setReg_dt(item.getReg_dt());
            // 여기에 다른 필드를 추가하세요
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(reservationItemDTOs);
    }
	// 예약 아이템 추가
	@PutMapping("/insertReserve_item")
	public ResponseEntity<Map<String, Object>> insertReserve(@RequestBody Map<String, Object> data) throws ParseException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	    int count = 1;
	    while (data.containsKey("title" + count)) {
	        String title = data.get("title" + count).toString();
	        String status = data.get("status" + count).toString();
	        int price = Integer.parseInt(data.get("price" + count).toString());
	        String stringreg_dt = data.get("reg_dt" + count).toString().replace("T", " ");
	        Date reg_dt = dateFormat.parse(stringreg_dt);

	        Reservation_item reservationItem = new Reservation_item();
	        reservationItem.setRno(Long.parseLong(data.get("rno").toString())); // rno 설정
	        reservationItem.setTitle(title);
	        reservationItem.setStatus(status);
	        reservationItem.setPrice(price);
	        reservationItem.setReg_dt(reg_dt);

	        // 데이터베이스에 INSERT 쿼리 실행
	        repository.save(reservationItem);

	        count++;
	    }
	    Map<String, Object> latestData = new HashMap<>();
	    Reservation_item latestReserv = repository.findTopByOrderByRnoDesc();
	    latestData.put("latestReserv_item", latestReserv);
	    return ResponseEntity.ok(latestData);
	}
	
	// 예약 취소
	@PutMapping("/cancelReservation_item")
	public ResponseEntity<?> cancelReservation_item(@RequestBody Map<String, Object> data) {
	    Long rno = Long.parseLong(data.get("rno").toString());
	    Long ino = Long.parseLong(data.get("ino").toString());
	    Date cancel_dt = new Date();
	    repository.updateStatusByRnoAndIno(rno,ino, "CANCEL", cancel_dt);
	    return ResponseEntity.ok().build();
	}
	
	// 예약 삭제
	@DeleteMapping("/deleteReservation_item")
	public ResponseEntity<?> deleteReservation_item(@RequestBody Map<String, Object> data) {
	    Long rno = Long.parseLong(data.get("rno").toString());
	    Long ino = Long.parseLong(data.get("ino").toString());
	    repository.deleteByRnoAndIno(rno,ino);
		return ResponseEntity.ok().build();
	}
	

}
