package com.movv.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.movv.entity.Reservation;
import com.movv.entity.Reservation_item;

import jakarta.transaction.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	public List<Reservation> findAll();
	
	public List<Reservation> findReservationByStatus(String status);
	
    @Query(value = "SELECT r.rno AS rno, r.title AS reservation_title, r.kor_name, r.eng_name, r.tel, r.status AS reservation_status, r.stdt, r.eddt, r.price AS reservation_price, r.info, r.cancel_dt AS reservation_cancel_dt, r.reg_dt AS reservation_reg_dt, ri.ino, ri.status AS item_status, ri.price AS item_price, ri.title AS item_title, ri.cancel_dt AS item_cancel_dt, ri.reg_dt AS item_reg_dt "
            + "FROM reservation r "
            + "JOIN reservation_item ri ON r.rno = ri.rno "
            + "WHERE r.status = :status "
            + "ORDER BY r.reg_dt DESC", nativeQuery = true)
    List<Map<String, Object>> findReservationsByStatus(@Param("status") String status);
    
    public Reservation findTopByOrderByRnoDesc();
    
    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.status = :status, r.cancel_dt = :cancel_dt WHERE r.rno = :rno")
    void updateStatusByRno(@Param("rno") Long rno, @Param("status") String status, @Param("cancel_dt") Date cancel_dt);

    @Modifying
    @Transactional
    @Query("delete from Reservation r WHERE r.rno = :rno")
    void deleteByRno(@Param("rno") Long rno);

}
