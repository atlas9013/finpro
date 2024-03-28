package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusBusRoad;
import com.example.demo.entity.BusRoad;
import com.example.demo.entity.BusStation;
import com.example.demo.entity.Member;
import com.example.demo.service.BusService;
import com.example.demo.service.Bus_BusroadService;
import com.example.demo.service.BusroadService;
import com.example.demo.service.BusstationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
public class BusroadController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;

	@Autowired
	private BusService busservice;
	
	@Autowired
	private BusroadService bs;
	
	@Autowired
	private BusService buss;
	
	@Autowired
	private BusstationService bss;

	@Autowired
	private Bus_BusroadService bbs;
	//버스노선
	@GetMapping("/busroad/list/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM,HttpSession session) {
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.count();
		
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		
		Member user = (Member) session.getAttribute("user");
		model.addAttribute("list", bs.busroad_list_count(start, end)); 
		model.addAttribute("totalPage", totalPage);
		System.out.println("user:"+user);
		model.addAttribute("user",user);
		return "/busroad/list";
	}

	
	@GetMapping("/busroad/insert")
	public void insertForm(Model model) {	
		System.out.println("insert폼작동");
		List<BusStation> list = bss.listBusstation(); //stationno 받아오기
		List<Bus> buslist = buss.listBus(); //stationno 받아오기
		
		model.addAttribute("list", list);
		model.addAttribute("buslist", buslist);
	}
	
	@PostMapping("/busroad/insert")
	public String insertSubmit(BusRoad b, HttpServletRequest req, @RequestParam("plus") String plus, HttpSession session) {
		String view = "redirect:/busroad/list/1";
		Bus bus = busservice.getBus(b.getBusno());
		//로그인사람의 정보를 가져오기위해
		Authentication authentication
		= SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		String id = user.getUsername();
		b.setArrival(bss.getBusStation_no(b.getArrival()).getStationno());
		b.setDeparture(bss.getBusStation_no(b.getDeparture()).getStationno());
		b.setId(id);
		
		if(bus.getGrade().equals("일반")){//일반버스일때 가격
			int price=5000;
			b.setPrice(price*b.getTottime());
		}else if(bus.getGrade().equals("우등")){//우등버스일때 가격
			int price=8000;
			b.setPrice(price*b.getTottime());	
		}
		
		if(plus.equals("plural")){//복수 노선 만들시
			int dip_time= Integer.parseInt(req.getParameter("dip_time"));//배차시간 가져오기 
			bs.insert(b);//기본적으로 선택된 bus노선 만들어주기
			 
			int a_time = b.getArrtime();
			int bus_count=0; //버스개수
			
			 for(int i=a_time; i<=22; i++){//필요한 버스 갯수 
				a_time=b.getTottime()+dip_time;
				if(a_time >=22){//페이지에 출발시간이 22시이후로는 설정못하게 되있어서 출발시간이 22시간 까지만 노선만들기위해
					break;
				}
				bus_count++;

			 }
			 int count=bbs.bus_no_list_null_count();//사용중이지않은 버스 갯수
				 
				 if(count < bus_count){//버스개수가 부족할떄(만들노선보다 노선에사용중인 버스가 많을떄)
					 //버스추가후 노선생성
					for(int j=0; j< bus_count; j++){
						 busservice.insertBus_normal();//일반 버스 생성(버스가 노선갯수보다 하나더 만들어지게 설계됨)
						 BusBusRoad bb1=bbs.bus_no_null_rownum();//bus_no 값 하나씩 출력하기 위해서
						 b.setBusno(bb1.getBusno());//bus_no 값을 bbdto1 객체에서 받아와 하나씩 넣어줌
						 b.setArrtime(b.getArrtime()+b.getTottime()+dip_time);//출발시간 지속적으로 변경
						
						 if(b.getArrtime()>22){
							 break;
						 }
						 bs.insert(b);

					 }
				 }else if(count>=bus_count){//버스갯수가 충분할때
					 for(int j=0; j<bus_count; j++){
						 BusBusRoad bb2=bbs.bus_no_null_rownum();//bus_no 값 하나씩 출력하기 위해서
						 b.setBusno(bb2.getBusno());
						 b.setArrtime(b.getArrtime()+b.getTottime()+dip_time);
						 if(b.getArrtime()>22){
							 break;
						 }
						 bs.insert(b);
					 }
				 }
			 
		}else{
			 bs.insert(b);
		}
		return view;
	}

	@GetMapping("/busroad/delete/{roadno}")
	public String delete(@PathVariable("roadno") int roadno) {
		String view = "redirect:/busroad/list/1";
		bs.deleteBusroad(roadno);
		return view;
	}
	
	@GetMapping("/busroad/update/{roadno}")
	public String updateForm(@PathVariable("roadno") int roadno, Model model, HttpServletRequest req) {
		BusRoad br = bs.getBusroad(roadno);
		BusStation arr_br= bss.getBusstation(br.getArrival());//넘어온 busno 값에대한 station 테이블에 station_no 값에 따른 station_name 찾기
		BusStation dep_br= bss.getBusstation(br.getDeparture());//넘어온 busno 값에대한 station 테이블에 station_no 값에 따른 station_name 찾기
		br.setArrival(arr_br.getStationno());//넘어온 busno 값을 출발지  숫자->한글
		br.setDeparture(dep_br.getStationno());//넘어온 busno 에 대한 도착지 숫자->한글
		
		List<Bus> buslist = buss.listBus(); 
		List<BusStation> busstation_list = bss.listBusstation(); //station_no 받아오기
		
		model.addAttribute("br",br);
		model.addAttribute("buslist",buslist);
		model.addAttribute("busstation_list",busstation_list);
		model.addAttribute("arr_br",arr_br);//창에서 조건문 쓰기위해 저장
		model.addAttribute("dep_br",dep_br);//창에서 조건문 쓰기위해 저장

		model.addAttribute("busroad", bs.getBusroad(roadno));
		return "/busroad/update";
	}
	@PostMapping("/busroad/update")
	public String bus_updateSubmit(HttpServletRequest req, BusRoad b,@RequestParam("arrival") int arrival,@RequestParam int departure) {
		String view = "redirect:/busroad/list/1";

//		b.setArrival(bss.getBusStation_no(arrival).getStationno());
//		b.setDeparture(bss.getBusStation_no(departure).getStationno());
		
		bs.updateBusroad(b); 
		return view;
	}
}
