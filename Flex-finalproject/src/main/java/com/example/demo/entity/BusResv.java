package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "busresv")
public class BusResv {
	@Id
	private int resvno;
	private String id;
	private int busno;
	private int roadno;
	private String seat;
	private int  usepoint;
	private int savepoint;
	private String resvdate;
	private int price;
}
