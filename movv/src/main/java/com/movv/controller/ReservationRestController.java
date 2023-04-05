package com.movv.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movv.entity.Reservation;
import com.movv.entity.Reservation_item;
import com.movv.repository.ReservationRepository;
import com.movv.repository.Reservation_itemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import net.minidev.json.JSONObject;

@RestController
public class ReservationRestController {

	@Autowired
	private ReservationRepository repository;
	@Autowired
	private Reservation_itemRepository itemRepository;
	
//	// 예약 확인 (Map 사용해서 처리)
//	@PostMapping("/checkReserved")
//	public List<Map<String, Object>> checkReserved(@RequestBody Map<String, String> request){
//		String status = request.get("status");
//		List<Reservation> list_raw = repository.findReservationByStatus(status);
//		List<Map<String, Object>> list=new ArrayList<>();
//		for (Reservation reservation : list_raw) {
//		    Map<String, Object> map = new HashMap<>();
//		    map.put("rno", reservation.getRno());
//		    map.put("title", reservation.getTitle());
//		    map.put("kor_name", reservation.getKor_name());
//		    map.put("eng_name", reservation.getEng_name());
//		    map.put("tel", reservation.getTel());
//		    map.put("status", reservation.getStatus());
//		    map.put("stdt", reservation.getStdt());
//		    map.put("eddt", reservation.getEddt());
//		    map.put("price", reservation.getPrice());
//		    map.put("info", reservation.getInfo());
//		    map.put("cancel_dt", reservation.getCancel_dt());
//		    map.put("reg_dt", reservation.getReg_dt());
//		    // Add the map to the list
//		    list.add(map);
//		}
////	    list.sort(Comparator.comparing((Map<String, Object> m) -> {
////	        Object timestamp = m.get("reg_dt");
////	        return timestamp != null ? ((Timestamp) timestamp).toLocalDateTime() : null;
////	    }, Comparator.nullsLast(Comparator.reverseOrder())));
//		return list;
//	}
	
	// 예약확인 (DTO 사용해서 처리한 방법)
    @PostMapping("/checkReserved")
    public ResponseEntity<List<Reservation>> selectReservationByStatus(@RequestBody Map<String, String> request) {
    	String status = request.get("status");
    	List<Reservation> reservations = repository.findReservationByStatus(status);
        List<Reservation> list = reservations.stream().map(reservation -> {
            Reservation dto = new Reservation();
            dto.setRno(reservation.getRno());
            dto.setTitle(reservation.getTitle());
            dto.setKor_name(reservation.getKor_name());
            dto.setEng_name(reservation.getEng_name());
            dto.setTel(reservation.getTel());
            dto.setStatus(reservation.getStatus());
            dto.setStdt(reservation.getStdt());
            dto.setEddt(reservation.getEddt());
            dto.setPrice(reservation.getPrice());
            dto.setInfo(reservation.getInfo());
            dto.setCancel_dt(reservation.getCancel_dt());
            dto.setReg_dt(reservation.getReg_dt());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(reservations);
    }
	
	
	// 전체 데이터로 예약 확인
	@PostMapping("/checkttlReserved")
	public List<Map<String, Object>> checkttlReserved(@RequestBody Map<String, String> request) {
	    String status = request.get("status");
	    List<Map<String, Object>> list = repository.findReservationsByStatus(status);
//	    list.sort(Comparator.comparing((Map<String, Object> m) -> {
//	        Object timestamp = m.get("reg_dt");
//	        return timestamp != null ? ((Timestamp) timestamp).toLocalDateTime() : null;
//	    }, Comparator.nullsLast(Comparator.reverseOrder())));
	    return list;
	}
	
	// 예약 추가
	@PutMapping("/insertReserve")
	public ResponseEntity<Map<String, Object>> insertReserve(@RequestBody Map<String, Object> data) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String title=data.get("title").toString();
		String kor_name=data.get("kor_name").toString();
		String eng_name=data.get("eng_name").toString();
		String tel=data.get("tel").toString();
		String status=data.get("status").toString();
		String stringstdt = data.get("stdt").toString().replace("T", " ");
		Date stdt = dateFormat.parse(stringstdt);
		String stringeddt=data.get("eddt").toString().replace("T", " ");
		Date eddt=dateFormat.parse(stringeddt);
		int price = Integer.parseInt(data.get("price").toString());
		Map<String, Object> infoMap = (Map<String, Object>) data.getOrDefault("info", new HashMap<>());
	    int adult = Integer.parseInt(infoMap.getOrDefault("adult", "0").toString());
	    int child = Integer.parseInt(infoMap.getOrDefault("child", "0").toString());
	    JSONObject infoJson = new JSONObject();
	    infoJson.put("adult", adult);
	    infoJson.put("child", child);
	    String info = infoJson.toJSONString();
		String stringreg_dt=data.get("reg_dt").toString().replace("T", " ");
		Date reg_dt=dateFormat.parse(stringreg_dt);
		
		Reservation reservation = new Reservation();
		reservation.setTitle(title);
		reservation.setKor_name(kor_name);
		reservation.setEng_name(eng_name);
		reservation.setTel(tel);
		reservation.setStatus(status);
		reservation.setStdt(stdt);
		reservation.setEddt(eddt);
		reservation.setPrice(price);
		reservation.setInfo(info);
		reservation.setReg_dt(reg_dt);

		// 데이터베이스에 INSERT 쿼리 실행
		repository.save(reservation);
		Map<String, Object> latestData = new HashMap<String,Object>();
		Reservation latestReserv=repository.findTopByOrderByRnoDesc();
		latestData.put("latestReserv", latestReserv);
		System.out.println(latestData.get("latestReserv"));
		return ResponseEntity.ok(latestData);
	}
	
	// 예약 취소
	@PutMapping("cancelReservation")
	public ResponseEntity<?> cancelReservation(@RequestBody Map<String, Object> data) {
	    Long rno = Long.parseLong(data.get("rno").toString());
	    Date cancel_dt = new Date();
	    repository.updateStatusByRno(rno, "CANCEL",cancel_dt);
	    itemRepository.updateStatusByRno(rno, "CANCEL",cancel_dt);
	    return ResponseEntity.ok().build();
	}
	
	// 예약 삭제
	@DeleteMapping("deleteReservation")
	public ResponseEntity<?> deleteReservation(@RequestBody Map<String, Object> data) {
		Long rno = Long.parseLong(data.get("rno").toString());
		List<Reservation_item> list_raw= itemRepository.findReservation_itemByRno(rno);
		if(list_raw!=null) {
			itemRepository.deleteByRno(rno);
		}
	    repository.deleteByRno(rno);
		return ResponseEntity.ok().build();
	}
}
