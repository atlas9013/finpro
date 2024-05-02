package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "hotelresv")
public class HotelResv {
	@Id
	private int hotelresvno;
	private String id;
	private int hotelno;
	private String roomno;
	private int usepoint;
	private int savepoint;
	private String startresvdate;
	private String endresvdate;
	private String resvdate;
	private int price; //추가

}
