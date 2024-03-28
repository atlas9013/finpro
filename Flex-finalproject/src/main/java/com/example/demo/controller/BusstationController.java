package com.example.demo.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusStation;
import com.example.demo.entity.Goods;
import com.example.demo.service.BusService;
import com.example.demo.service.BusstationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;

@Controller
public class BusstationController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;

	@Autowired
	private BusstationService bs;

	@GetMapping("/busstation/list/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM) {
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.count(); //count는 기본적으로제공해줘

		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;

		System.out.println("start:"+start);
		System.out.println("end:"+end);

		model.addAttribute("list", bs.busstation_list_count(start, end)); 
		model.addAttribute("totalPage", totalPage);

		return "/busstation/list";
	}

	@GetMapping("/busstation/insert")
	public void insertForm(@RequestParam(value = "stationno", defaultValue = "0")int stationno, Model model) {		
		model.addAttribute("stationno", stationno);
	}

	@PostMapping("/busstation/insert")
	public String insertSubmit(BusStation b, HttpServletRequest request) {
		//파일
		String path = request.getServletContext().getRealPath("/images");
		System.out.println("path:"+path);
		String filename = null;
		MultipartFile uploadFile = b.getUploadFile();
		filename = uploadFile.getOriginalFilename();
		b.setFilename(filename);

		String view = "redirect:/busstation/list/1";
		if(filename != null && !filename.equals("")) {
			try {
				byte []data = uploadFile.getBytes();
				FileOutputStream fos = new FileOutputStream(path + "/" + filename);
				fos.write(data);
				fos.close();
			}catch (Exception e) {
				System.out.println("예외발생:"+e.getMessage());
			}
		}
		bs.insert(b);

		return view;
	}

	@GetMapping("/busstation/delete/{stationno}")
	public String delete(@PathVariable("stationno") int stationno, HttpServletRequest request) {
		String view = "redirect:/busstation/list/1";
		String path = request.getServletContext().getRealPath("/images/terminal");
		String oldFname = bs.getBusstation(stationno).getFilename();
		System.out.println(oldFname);
		bs.deleteBusstation(stationno);
		try {
			File file = new File(path +"/"+oldFname);
			file.delete();
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return view;
	}

	@GetMapping("/busstation/update/{stationno}")
	public String busstation_update(@PathVariable int stationno, Model model) {
		model.addAttribute("busstation", bs.getBusstation(stationno));
		return "/busstation/update";
	}

	@PostMapping("/busstation/update")
	public String busstation_updateSubmit(BusStation b, HttpServletRequest request, Model model) {
		String path = request.getServletContext().getRealPath("/images/terminal");
		System.out.println("path:"+path);
		String fname = null;
		String oldFname= b.getFilename();
		b.setFilename(oldFname);
		System.out.println("oldFname이 설정됨"+oldFname);
		MultipartFile uploadFile = b.getUploadFile();
		fname = uploadFile.getOriginalFilename();
		System.out.println("fname: "+fname);

		if(fname != null && !fname.equals("")) {   //첨부파일이 있는 경우
			try {
				FileOutputStream fos = new FileOutputStream(path+"/"+fname);            
				FileCopyUtils.copy(uploadFile.getBytes(), fos);
				fos.close();
				b.setFilename(fname);
				System.out.println("fname이 설정됨"+fname);
				bs.updateBusstation(b);
				File file = new File(path + "/" + oldFname);
				file.delete();
			}catch (Exception e) {
				System.out.println("파일 업로드 예외발생:"+e.getMessage());   
			}
		}else {   //첨부파일이 없는 경우
			bs.updateBusstation(b);
		}
		String view = "redirect:/busstation/list/1";
		return view;
	}
	
	//-------------------------------버스user-----------------------------------------
		@GetMapping(value="/user_busstation/info")
		public void bus_station_info(Model model){
			List<BusStation> busstation_list = bs.listBusstation();
			model.addAttribute("busstation_list",busstation_list);
		}
		
		@GetMapping(value="/user_busstation/map/{stationno}")
		public String bus_station_info_map(@PathVariable int stationno){
			String view = "/user_busstation/map";
			return view;
		}
		
		@GetMapping(value="/user_busstation/detail/{stationno}") //버스정류소 디테일 페이지
		public String bus_station_info_detail(@PathVariable("stationno") int stationno, Model model){
			BusStation b =bs.getBusstation(stationno);
			
			String view = "/user_busstation/detail";
			model.addAttribute("b", b);
			
			return view;
		}
}
