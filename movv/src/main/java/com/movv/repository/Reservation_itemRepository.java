package com.movv.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.movv.entity.Reservation_item;

import jakarta.transaction.Transactional;

@Repository
public interface Reservation_itemRepository extends JpaRepository<Reservation_item, Long> {
	
	public List<Reservation_item> findAll();
	
	public List<Reservation_item> findReservation_itemByRno(Long rno);
	
	public List<Reservation_item> findReservation_itemByStatus(String status);
	
	public Reservation_item findTopByOrderByRnoDesc();
	
	@Modifying
	@Transactional
	@Query("UPDATE Reservation_item ri SET ri.status = :status, ri.cancel_dt = :cancel_dt WHERE ri.rno = :rno")
	void updateStatusByRno(@Param("rno") Long rno, @Param("status") String status, @Param("cancel_dt") Date cancel_dt);
	
    @Modifying
    @Transactional
    @Query("UPDATE Reservation_item ri SET ri.status = :status, ri.cancel_dt = :cancel_dt WHERE ri.rno = :rno AND ri.ino = :ino")
    void updateStatusByRnoAndIno(@Param("rno") Long rno,@Param("ino") Long ino, @Param("status") String status, @Param("cancel_dt") Date cancel_dt);

    
    @Modifying
    @Transactional
    @Query("delete from Reservation_item ri WHERE ri.rno = :rno")
    void deleteByRno(@Param("rno") Long rno);
    
    @Modifying
    @Transactional
    @Query("delete from Reservation_item ri WHERE ri.rno = :rno and ri.ino = :ino ")
    void deleteByRnoAndIno(@Param("rno") Long rno, @Param("ino") Long ino);
}
