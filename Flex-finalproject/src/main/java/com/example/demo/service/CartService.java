package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CartDAO;
import com.example.demo.entity.Cart;

import lombok.Setter;

@Service
@Setter
public class CartService {
	@Autowired
	private CartDAO dao;
	
	public List<Cart> listCart(String id){
		return dao.findById(id);
	}
	
	
	public void insert(Cart c) {
//		c.setNo(dao.getNextNo());
		c.setQty(1);
		
		String id = c.getId();
		int gno = c.getGno();
		Cart old = dao.findByIdAndGno(id, gno);
		
		if(old != null) {
			c.setQty(old.getQty()+1);
			c.setNo(old.getNo());
		}
		dao.save(c);
	}
}