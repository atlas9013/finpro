package com.example.demo.controller;

import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Member;
import com.example.demo.entity.Payment;
import com.example.demo.service.GoodsService;
import com.example.demo.service.MemberService;
import com.example.demo.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class GoodsController {
	@Autowired
	private GoodsService gs;
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private PaymentService ps;
	
	@GetMapping("/goods/payment")
	@ResponseBody
	public String payment(Payment p) {
		ps.insert(p);
		return "ok";
	}
	
	@GetMapping("/goods/order/{no}")
	public String order(@PathVariable("no") int no, Model model) {
		String view = "/goods/order";
		model.addAttribute("g", gs.getGoods(no));
		return view;
	}
	
	
	@GetMapping("/goods/detail/{no}")
	public String detail(@PathVariable("no") int no, Model model) {
		model.addAttribute("g", gs.getGoods(no));
		return "/goods/detail";
	}
	
	
	@GetMapping("/goods/list")
	public void list(Model model, HttpSession session) {
		Authentication authentication
		= SecurityContextHolder.getContext().getAuthentication();
		
		User user =(User) authentication.getPrincipal();
		String id = user.getUsername();
		Member m = memberDAO.findById(id).get();
		session.setAttribute("user", m);
		
		model.addAttribute("list", gs.findAll());
	}
	
	@GetMapping("/goods/insert")
	public void insertForm() {	
		System.out.println("insert폼작동");
	}
	
	@PostMapping("/goods/insert")
	public String insertSubmit(Goods g, HttpServletRequest request) {
		String view = "redirect:/goods/list";
		String path = request.getServletContext().getRealPath("/images");
		System.out.println("path:"+path);
		String fname = null;
		MultipartFile uploadFile = g.getUploadFile();
		fname = uploadFile.getOriginalFilename();
		if(fname != null && !fname.equals("")) {
			try {
				FileOutputStream fos = new FileOutputStream(path+"/"+fname);
				FileCopyUtils.copy(uploadFile.getBytes(), fos);
				fos.close();
				g.setFname(fname);
			}catch (Exception e) {
				System.out.println("예외발생:"+e.getMessage());
			}
		}
		gs.insert(g);
		return view;
	}
}
