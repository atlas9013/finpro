package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "cart")
@SequenceGenerator(name="SEQ_CART_GEN",sequenceName = "SEQ_CART", initialValue = 1, allocationSize = 1) //스타트1부터
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART_GEN")
	private int no;
	private String id;
	private int gno;
	private int qty;
}