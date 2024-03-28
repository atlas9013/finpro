package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Member;
import com.example.demo.service.CartService;
import com.example.demo.service.GoodsService;

import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class OrdersController {
	@Autowired
	private CartService cs;	
	
	@Autowired
	private GoodsService gs;
	
	@GetMapping("/orders/checkout")
	public void checkout(Model model, HttpSession session) {
		String id = ((Member)session.getAttribute("user")).getId();
		List<Cart> list = cs.listCart(id);
		int total = 0;
		String name = "";		
		for(Cart c:list) {
			int qty = c.getQty();
			int gno = c.getGno();
			Goods g = gs.getGoods(gno);
			int price = g.getPrice();
			total = total + (qty*price);
			if(name.equals("")) {
				name = g.getItem()+"외 "+ list.size()+"건";
			}
		}
		model.addAttribute("list",list );
		model.addAttribute("total",total );
		model.addAttribute("name",name );
	}
}