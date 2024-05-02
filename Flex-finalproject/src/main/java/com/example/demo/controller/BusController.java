package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Bus;
import com.example.demo.entity.Member;
import com.example.demo.service.BusService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
public class BusController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;
	public int pageGROUP = 5;

	@Autowired
	private BusService bs;

	//버스
	@GetMapping("/bus/main")
	public String BusAD(HttpServletRequest req) {
		return "/bus/main";
	}
	
	@GetMapping("/bus/list/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM, HttpSession session) {
		String msg = "";
		String url = "";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.count(); //count는 기본적으로제공해줘
		
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		
		int startNum = totalRecord-((pageNUM-1)*pageSIZE);
		int pageCount = totalRecord/pageSIZE + (totalRecord%pageSIZE == 0 ? 0 : 1);
		pageGROUP = 5;
		int startPage = (pageNUM-1)/pageGROUP * pageGROUP + 1;
		int endPage = startPage + pageGROUP - 1;
		if (endPage>pageCount) endPage = pageCount;
		
		model.addAttribute("list", bs.bus_list_count(start, end)); 
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalRecord", totalRecord);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("startNum", startNum);
		model.addAttribute("pageGROUP", pageGROUP);

		return "/bus/list";
		}
	}

//	@GetMapping("/bus/insert/{busno}")
//	public String insertfrom(@RequestParam(value = "busno", defaultValue = "0")int busno, Model model) {
//		model.addAttribute("no", busno);
//		return "/bus/insert";
//	}
	
	@GetMapping("/bus/insert")
	public void insertForm() {	
		System.out.println("insert폼작동");
	}
	
	@PostMapping("/bus/insert")
	public String insertSubmit(Bus b, HttpSession session, Model model) {
		String msg = "";
		String url = "";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
		String view = "redirect:/bus/list/1";
		bs.insert(b);
		return view;
		}
	}

	@GetMapping("/bus/delete/{busno}")
	public String delete(@PathVariable("busno") int busno, HttpSession session, Model model) {
		String msg = "";
		String url = "";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
			String view = "redirect:/bus/list/1";
			bs.deleteBus(busno);
			return view;
		}
	}
	
	@GetMapping("/bus/update/{busno}")
	public String bus_update(@PathVariable int busno, Model model, HttpSession session) {
		String msg = "";
		String url = "";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
			model.addAttribute("bus", bs.getBus(busno));
			return "/bus/update";
		}
	}
	@PostMapping("/bus/update")
	public String bus_updateSubmit(Bus b, Model model, HttpSession session) {
		String msg = "";
		String url = "";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
			String view = "redirect:/bus/list/1";
			bs.updateBus(b);
			return view;
		}
	}
}
