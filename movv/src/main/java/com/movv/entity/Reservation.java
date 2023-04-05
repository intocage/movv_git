package com.movv.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Reservation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long rno; //예약키
	
	private String title; //상품명
	
	private String kor_name; //한국명
	
	private String eng_name; //영문명
	
	private String tel; //전화번호
	
	private String status; //상태

	private Date stdt; //시작일
	
	private Date eddt; //종료일
	
	private int price; //금액
	
	@Column(columnDefinition = "json")
	private String info; //info
	
	private Date cancel_dt; //취소일
	
	private Date reg_dt; //등록일
	
	@Override
	public String toString() {
	    return "Reservation{" +
	            "rno=" + rno +
	            ", title='" + title + '\'' +
	            ", kor_name='" + kor_name + '\'' +
	            ", eng_name='" + eng_name + '\'' +
	            ", tel='" + tel + '\'' +
	            ", status='" + status + '\'' +
	            ", stdt=" + stdt +
	            ", eddt=" + eddt +
	            ", price=" + price +
	            ", info='" + info + '\'' +
	            ", cancel_dt=" + cancel_dt +
	            ", reg_dt=" + reg_dt +
	            '}';
	}
	
	// 어째선지 Data 어노테이션이 안먹힙니다...

	public Long getRno() {
		return rno;
	}

	public void setRno(Long rno) {
		this.rno = rno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKor_name() {
		return kor_name;
	}

	public void setKor_name(String kor_name) {
		this.kor_name = kor_name;
	}

	public String getEng_name() {
		return eng_name;
	}

	public void setEng_name(String eng_name) {
		this.eng_name = eng_name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStdt() {
		return stdt;
	}

	public void setStdt(Date stdt) {
		this.stdt = stdt;
	}

	public Date getEddt() {
		return eddt;
	}

	public void setEddt(Date eddt) {
		this.eddt = eddt;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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
