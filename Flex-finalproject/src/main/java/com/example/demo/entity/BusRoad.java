package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "busroad")
public class BusRoad {
	@Id
	private int roadno;
	private int busno;
	private String id;
	@Column(name = "arrival")
	private int arrival;
	@Column(name = "departure")
	private int departure;
	private int price;
	@Column(name = "arrtime")
	private int arrtime;
	@Column(name = "tottime")
	private int tottime;

}
