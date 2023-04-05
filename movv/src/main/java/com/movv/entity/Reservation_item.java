package com.movv.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
public class Reservation_item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ino; //예약 아이템키,PK
	
	private Long rno; //예약키,FK
	
//	@Column(length = 10)
	private String status; //상태
	
	private int price; //가격
	
//	@Column(length = 100)
	private String title; //아이템명
	
	private Date cancel_dt; //취소일자
	
	private Date reg_dt; //등록일
	
    @Override
    public String toString() {
        return "Reservation_item{" +
                "ino=" + ino +
                ", rno=" + rno +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", cancel_dt=" + cancel_dt +
                ", reg_dt=" + reg_dt +
                '}';
    }
    
	// 어째선지 Data 어노테이션이 안먹힙니다...

	public Long getIno() {
		return ino;
	}

	public void setIno(Long ino) {
		this.ino = ino;
	}

	public Long getRno() {
		return rno;
	}

	public void setRno(Long rno) {
		this.rno = rno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCancel_dt() {
		return cancel_dt;
	}

	public void setCancel_dt(Date cancel_dt) {
		this.cancel_dt = cancel_dt;
	}

	public Date getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(Date reg_dt) {
		this.reg_dt = reg_dt;
	}

    
}
