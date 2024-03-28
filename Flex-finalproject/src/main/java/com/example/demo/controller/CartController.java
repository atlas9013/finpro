package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Member;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	private CartService cs;
	
	@GetMapping("/cart/add/{no}")
	public String add(@PathVariable("no") int no, HttpSession session, Model model) {
		String view="/cart/add";
		//로그인사람의 정보를 가져오기위해
		Authentication authentication
		= SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		String id = user.getUsername();
		
		Cart c = new Cart();
		c.setId(id);
		c.setGno(no);
		
		model.addAttribute("id", id);
		cs.insert(c);
		return view;
	}
	
	//유저별 장바구니 조회
	@GetMapping("/cart/list")
	public String detailCart(Model model, HttpSession session) {
		String view = "/cart/list";
		String id = ((Member)session.getAttribute("user")).getId();
		model.addAttribute("list",cs.listCart(id));
		return view;
	}
}
