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
@Table(name = "hotelboard")
public class Hotelboard {
	
	@Id
	private int hotelboardno;
	private String id;
	private int hotelno;
	private int readcount;
	private String filename;
	private String content;
	private String regdate;
	private int regroup;
	private int restep;
	private int relevel;
	private String title;

	@Transient
	private MultipartFile uploadFile;
}
