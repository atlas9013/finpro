package com.example.demo.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	
	@Autowired
	private MemberDAO memberDAO;
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@GetMapping("/")
	public String index(Locale locale, Model model, HttpSession session) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		Authentication authentication
		= SecurityContextHolder.getContext().getAuthentication();
		
		User user =(User) authentication.getPrincipal();
		String id = user.getUsername();
		Member m = memberDAO.findById(id).get();
		session.setAttribute("user", m);
		return "bus_main";
	}
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/home2")
	public String home2() {
		return "home2";
	}
}

