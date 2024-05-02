package com.example.demo.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "room")
public class Room {
	
	@Id
	private String roomno;
	private int hotelno; 
	private String name;
	private int roomsize;
	private int sleeps;
	private String item;
	private String filename; 
	private int price;
	private int rooms;

	@Transient
	private MultipartFile uploadFile;
}
