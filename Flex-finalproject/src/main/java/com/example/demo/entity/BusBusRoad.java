package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "busbusroad")
@Setter
@Getter
@AllArgsConstructor 
@NoArgsConstructor // 위에 매개변수있는걸 설정했으니 기본도 설정해야-> 기본 생성자를 자동 생성함
public class BusBusRoad {
	@Id
	private String id;
	private int busno;
	private String grade; 
	private int seat;
	private int roadno;
	
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