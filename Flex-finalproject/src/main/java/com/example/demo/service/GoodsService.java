package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.GoodsDAO;
import com.example.demo.entity.Goods;

import lombok.Setter;

@Service
@Setter
public class GoodsService {
	@Autowired
	private GoodsDAO dao;
	
	public void insert(Goods g) {
		dao.save(g);
	}
	
	public List<Goods> findAll(){
		return dao.findAll();
	}
	
	public Goods getGoods(int no) {
		return dao.findById(no).get();
	}
}








