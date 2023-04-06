# movv_git
movv_git
![image](https://user-images.githubusercontent.com/116136538/229740204-e2126d8a-f6b1-48ef-8e09-c586b3de886c.png)

1. 데이터 입력 
  - 예약 데이터 입력시 예약_아이템 테이블에도 최소 1건 이상의 데이터 입력하는 api
  - status : RESERV
  <풀이>
  - form data를 활용하여 json stringfy 데이터로 입력합니다.
  - 예약당 아이템은 반드시 1개 이상 포함해야하므로, 하나의 아이템은 처음에 추가했으며, 아이템 추가하기 버튼으로 등록할 아이템
    추가 가능
  - 모든 데이터가 입력되지 않으면 등록하기 버튼을 누를수 없도록하여 데이터 유효성 확보
  - info컬럼은 선택이므로 각 항목을 선택하지 않으면 아래와 같이 처리하여 null을 넣도록 하였습니다.

````
	String info = data.get("info") == null ? null : data.get("info").toString();


````
  - reservation의 price는 선택사항이므로 유효성에서 제외하였으며 공백 데이터를 전송할경우 reservation_item들의 price 합계를 넣도록 했습니다.
````
	int count = 1;
	int price_sum=0;
	while (data.containsKey("title" + count)) {
		price_sum += Integer.parseInt(data.get("price" + count).toString());
		count++;
	}
	int price = data.get("price")!="" ? Integer.parseInt(data.get("price").toString()) : price_sum;

````

  - reservation의 등록이 완료되면 rno가 가장 큰 reservation행을 찾아 반환하여 front의 data에 rno항목을 추가해줍니다.
  - rno가 추가된 data로 reservation_item들을 등록하는데, 이때 front의 name속성에 숫자를 붙여 아래와 같이 활용했습니다.
````

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

````
  * 참조
  - entity에서 lombok의 @Data 어노테이션과 setter,gertter 어노테이션을 사용했는데도 getter가 안먹혀서 각 entity에
    getter&setter생성을 해줬습니다. 

2. 취소 : 예약 및 아이템 취소하는 api
  - status : CANCEL
  - cancel_dt
  <풀이>
  - 아래와 같이 jpa와 query를 사용하여 작성했습니다.
````

    @Modifying
    @Transactional
    @Query("UPDATE Reservation_item ri SET ri.status = :status, ri.cancel_dt = :cancel_dt WHERE ri.rno = :rno AND ri.ino = :ino")
    void updateStatusByRnoAndIno(@Param("rno") Long rno,@Param("ino") Long ino, @Param("status") String status, @Param("cancel_dt") Date cancel_dt);
    
    @PutMapping("/cancelReservation_item")
	  public ResponseEntity<?> cancelReservation_item(@RequestBody Map<String, Object> data) {
	    Long rno = Long.parseLong(data.get("rno").toString());
	    Long ino = Long.parseLong(data.get("ino").toString());
	    Date cancel_dt = new Date();
	    repository.updateStatusByRnoAndIno(rno,ino, "CANCEL", cancel_dt);
	    return ResponseEntity.ok().build();
	  }

````

  - reservation의 rno만 입력하면 한번에 모든 rno를 참조하는 reservation_item과 rno까지 status상태를 CANCEL로 하고, 취소 시간은 now를 사용했습니다.
````

  	// 예약 취소
	  @PutMapping("cancelReservation")
	  public ResponseEntity<?> cancelReservation(@RequestBody Map<String, Object> data) {
	    Long rno = Long.parseLong(data.get("rno").toString());
	    Date cancel_dt = new Date();
	    repository.updateStatusByRno(rno, "CANCEL",cancel_dt);
	    itemRepository.updateStatusByRno(rno, "CANCEL",cancel_dt);
	    return ResponseEntity.ok().build();
	  }

````

  - reservation의 rno와 reservation_item의 ino를 모두 입력받아 하나의 item만 취소하는 기능도 작성했습니다.

3. list 조회 api
  - 공통 : 입력일자 desc 
  - 입력 된 예약 상태값에 따른 예약 list 조회
    : 아이템 정보 포함
  <풀이>
  - 총 6개의 조회버튼을 작성 (reg_dt 내림차순 정렬)
    1) rno : RESERV - reservation table의 status가 RESERV인 모든 행 
    2) rno : CANCEL - reservation table의 status가 CANCEL인 모든 행
    3) ino : RESERV - reservation_item table의 status가 RESERV인 모든 행
    4) ino : CANCEL - reservation_item table의 status가 CANCEL인 모든 행
    5) total : RESERV - reservation, reservation_item을 rno로 inner조인하여 모든 컬럼을 표시, status가 RESERV인 모든 행
    6) total : CANCEL - reservation, reservation_item을 rno로 inner조인하여 모든 컬럼을 표시, status가 CANCEL인 모든 행

4. 삭제 api
  - 예약 데이터 삭제
  - 예약 아이템 데이터 삭제
  <풀이>
  - JPA를 활용하여 삭제 메서드를 작성했습니다.
  - rno를 입력시 해당 rno와 rno를 참조하는 모든 reservation_item들도 삭제하는 기능을 작성했습니다.
  - rno와 ino를 모두 입력하여 특정 reservation_item만 삭제하는 기능도 작성했습니다.
  - 특이사항으로는 rno를 참조하는 reservation_item이 없을때 null pointer exception이 발생하여 아래와 같이 처리했습니다.
````

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
