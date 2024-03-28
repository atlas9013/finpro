package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Bus;
import com.example.demo.service.BusService;

import lombok.Setter;

@Controller
public class BusController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;

	@Autowired
	private BusService bs;

	//버스
	@GetMapping("/bus/list/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM) {
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.count(); //count는 기본적으로제공해줘
		
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		
		model.addAttribute("list", bs.bus_list_count(start, end)); 
		model.addAttribute("totalPage", totalPage);

		return "/bus/list";
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
	public String insertSubmit(Bus b) {
		String view = "redirect:/bus/list/1";
		bs.insert(b);
		return view;
	}

	@GetMapping("/bus/delete/{busno}")
	public String delete(@PathVariable("busno") int busno) {
		String view = "redirect:/bus/list/1";
		bs.deleteBus(busno);
		return view;
	}
	
	@GetMapping("/bus/update/{busno}")
	public String bus_update(@PathVariable int busno, Model model) {
		model.addAttribute("bus", bs.getBus(busno));
		return "/bus/update";
	}
	@PostMapping("/bus/update")
	public String bus_updateSubmit(Bus b) {
		String view = "redirect:/bus/list/1";
		bs.updateBus(b);
		return view;
	}
}
