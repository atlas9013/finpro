package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "busstation")
public class BusStation {
	
	@Id
	private int stationno;
	private String stationname;
	private String filename;
	private String address;
	
	@Transient
	private MultipartFile uploadFile;
}