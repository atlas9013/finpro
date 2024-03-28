package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "busresvbusroad")
@Setter
@Getter
public class BusResv_BusRoad {
	@Id
	private int busresvno;
	private String id;
	private int busno;
	private int roadno;
	private String seat;
	private int  usepoint;
	private int savepoint;
	private String resvdate;
	private int price;
	private int arrival;
	private int departure;
	private int arrtime;
	private int tottime;
	
}