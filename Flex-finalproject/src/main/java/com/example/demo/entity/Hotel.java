package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "hotel")
public class Hotel {
	@Id
	private int hotelno;
	private String name;
	private String address;
	private String phone;
	private String id;
	private String info;
	private int star;
	private String filename; 

	@Transient
	private MultipartFile uploadFile;

}
