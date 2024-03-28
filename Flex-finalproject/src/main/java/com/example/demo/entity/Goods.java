package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "goods")
public class Goods {
	@Id
	private int no;
	private String item;
	private int qty;
	private int price;
	private String fname;	
	@Transient
	private MultipartFile uploadFile;
}
