package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
public class Payment {
	@Id
	private int no;
	private String pay_method;
	private String merchant_uid;
	private String name;
	private int amount;
	private String buyer_id;
	private String buyer_email;
	private String buyer_name;
	private String buyer_tel;
	private String imp_uid;
	private String apply_num;
	private String resvno;
}
