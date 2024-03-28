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
import com.example.demo.entity.BusResv;
import com.example.demo.entity.BusRoad;
import com.example.demo.entity.BusStation;
import com.example.demo.entity.Member;
import com.example.demo.service.BusService;
import com.example.demo.service.Bus_BusroadService;
import com.example.demo.service.BusresvService;
import com.example.demo.service.BusroadService;
import com.example.demo.service.BusstationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
public class BusresvController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;

	@Autowired
	private BusService busservice;
	
	@Autowired
	private BusresvService bs;
	
	@Autowired
	private BusService buss;
	
	@Autowired
	private BusroadService busroadservice;
	
	@Autowired
	private BusstationService bss;

	@Autowired
	private Bus_BusroadService bbs;
	//버스예약
	
	@GetMapping("/busresv/list/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM) {
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.count();
		
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		
		List<BusResv> list = bs.busresv_list_count(start, end);

		model.addAttribute("list", list); 
		model.addAttribute("totalPage", totalPage);
		return "/busresv/list";
	}

	
	@GetMapping("/busresv/insert")
	public void insertForm(Model model, HttpSession session) {	
		System.out.println("insert폼작동");
		Member user = (Member) session.getAttribute("user");
		System.out.println("user:"+user);
		model.addAttribute("user",user);
		
		List<BusStation> list = bss.listBusstation(); //stationno 받아오기
		List<Bus> buslist = buss.listBus(); //stationno 받아오기
		List<BusRoad> busroadlist = busroadservice.listBusroad();
		model.addAttribute("list", list);
		model.addAttribute("busroadlist", busroadlist);
		model.addAttribute("buslist", buslist);
	}
	
	@PostMapping("/busresv/insert")
	public String insertSubmit(BusResv b) {
		String view = "redirect:/busresv/list/1";
		bs.insert(b);

		return view;
	}

	@GetMapping("/busresv/delete/{roadno}")
	public String delete(@PathVariable("roadno") int roadno) {
		String view = "redirect:/busresv/list/1";
		bs.delete(roadno);
		return view;
	}
	
	@GetMapping("/busresv/update/{resvno}")
	public String updateForm(@PathVariable("resvno") int resvno, Model model) {
		BusResv br = bs.getBusresv(resvno);
		model.addAttribute("br",br);
		
		return "/busresv/update";
	}
	@PostMapping("/busresv/update")
	public String updateSubmit(BusResv b) {
		String view = "redirect:/busresv/list/1";
		
		bs.update(b); 
		return view;
	}
	
	//버스 노선 조회 및 예약
	@GetMapping(value="/user_busresv/lookup") //버스 조회 및 예약 메인
	public void bus_user_resv_list(){
		
	}
	
	//출발지 선택 페이지
	@GetMapping(value="/user_busresv/arrival")
	public void bus_resv_user_arrival(Model model){
		List<BusStation> busstation_list = bss.listBusstation();
		model.addAttribute("busstation_list",busstation_list);
	}
	
	//출발지 선택 하기
	@GetMapping(value="/user_busresv/arrival_select/{arrival}")
	public String bus_resv_user_arrival_select(BusStation b,@PathVariable int arrival, Model model) {
		String view = "/user_busresv/arrival";
		
		List<BusStation> busstation_list = bss.listBusstation();
		if(arrival != 0){ 
			b = bss.getBusstation(arrival);
			model.addAttribute("arr_b",b);
			model.addAttribute("busstation_list",busstation_list);
		}
		return view;
	}
	
	//출발지 선택후
	@PostMapping(value="/user_busresv/arrival")
	public String bus_Resv_user_arrivalOk(HttpServletRequest req,@RequestParam int arrival, HttpSession session, Model model){
		String view = "/user_busresv/lookup";
		BusStation b = bss.getBusStation_no(arrival);
		
		session.setAttribute("arrb", b);
		
		if(session.getAttribute("depb")!=null){
			BusStation depdto=(BusStation)session.getAttribute("depb");
			model.addAttribute("dep_b",depdto);
		}
		
		model.addAttribute("arr_b",b);
		return view;
	}
	
	//도착지 페이지
	@GetMapping(value="/user_busresv/departure")
	public void bus_resv_user_departure(Model model) {
		List<BusStation> busstation_list = bss.listBusstation();
		model.addAttribute("busstation_list",busstation_list);
	}
	
	//도착지 선택 하기
	@GetMapping(value="/user_busresv/departure_select/{departure}")
	public String bus_resv_user_departure_select(BusStation b,@PathVariable int departure, Model model) {
		String view = "/user_busresv/departure";
		
		List<BusStation> busstation_list = bss.listBusstation();
		
		if(departure != 0){
			b = bss.getBusstation(departure);
			model.addAttribute("dep_b",b);
			model.addAttribute("busstation_list",busstation_list);
		}	
		return view;
	}
	
	//도착지 선택후
	@PostMapping(value="/user_busresv/departure")
	public String bus_rsev_user_departureOk(HttpServletRequest req ,@RequestParam int departure, HttpSession session, Model model){
		String view = "/user_busresv/lookup";
		
		BusStation b = bss.getBusStation_no(departure);
		session.setAttribute("depb", b);
		
		if(session.getAttribute("arrb")!=null){
			BusStation arrb=(BusStation) session.getAttribute("arrb");
			model.addAttribute("arrb",arrb);
		}
		
		model.addAttribute("dep_b",b);
		
		return view;
	}
	
	//출발지 검색
	@GetMapping(value="/user_busresv/arrival_find")
	public String bus_resv_arrival_find(@RequestParam("searchString") String searchString, Model model){
		String view = "/user_busresv/arrival";
		
		List<BusStation> list = bss.find_station(searchString);
		model.addAttribute("find_station",list);
		return view;
	}
	
	//도착지 검색
	@GetMapping(value="/user_busresv/departure_find")
	public String bus_resv_departure_find(@RequestParam("searchString") String searchString, Model model){
		String view = "/user_busresv/departure";
		
		List<BusStation> list = bss.find_station(searchString);
		model.addAttribute("find_station",list);
		return view;
	}
	
	//배차조회 list
	@GetMapping(value="/user_busresv/dispatch")
	public String bus_user_dispatch(BusRoad b,HttpSession session, Model model,@RequestParam String mode,@RequestParam int arrival,@RequestParam int departure,@RequestParam String grade,@RequestParam String one_date,@RequestParam String arr_date,@RequestParam String dep_date) {
		String view = "/user_busresv/dispatch";
		if(mode.equals("oneway")){//편도 선택 했을떄
			System.out.println("편도선택");
			List<BusBusRoad> dispatch_list = bs.list_seat_resv_user(arrival,departure,grade);//출발지,도착지,버스등급에 맞는 리스트를 5개씩출력
			System.out.println(dispatch_list);
			
//			if(grade.equals("전체")){//grade 가 전체 일때
//				dispatch_list = bs.listDispatch_resv_all(arrival, departure);//출발지,도착지 에 맞는 리스트를 5개씩 출력
//			}
//			model.addAttribute("dispatch_list", dispatch_list); 
//			model.addAttribute("mode",mode);
//			model.addAttribute("arrival",arrival);//페이지이동을위해 넘겨줌
//			model.addAttribute("departure",departure);//페이지이동을위해 넘겨줌
//			model.addAttribute("grade",grade);//페이지이동을위해 넘겨줌
//			model.addAttribute("one_date",one_date);
		}
		
//		if(mode.equals("twoway")){
//			List<BusBusRoad> arr_dispatch_list = bs.list_seat_resv_user(arrival,departure,grade);//출발지,도착지,버스등급에 맞는 리스트를 5개씩출력
//			List<BusBusRoad> dep_dispatch_list = bs.listDispatch_resv_reverse_count1(arrival, departure, grade);
//			
//			if(grade.equals("전체")){//grade 가 전체 일떄
//				arr_dispatch_list = bs.listDispatch_resv_all(arrival, departure);//출발지,도착지 에 맞는 리스트를 5개씩 출력
//				dep_dispatch_list = bs.listDispatch_resv_all_reverse(departure, arrival);
//			}
//			model.addAttribute("arr_dispatch_list",arr_dispatch_list);
//			model.addAttribute("dep_dispatch_list",dep_dispatch_list);
//		}
		
		
//		if(session.getAttribute("arr_seat")!=null){//왕복 노선 할때 출발일 부터 설정할때 선택한 좌석값 저장
//	String[] seat = seat;
//	int seat_no=seat.length;
//	session.setAttribute("seat",seat);
//	session.setAttribute("seat_no",seat_no);
//}
//if(session.getAttribute("dep_seat_dto")!=null){//왕복 노선 할때 도착일 부터 설정할때 선택한 좌석값 저장
////	String[] seat_reverse = seat_reverse;
//	int seat_no_reverse=seat_reverse.length;
//	session.setAttribute("seat_reverse",seat_reverse);
//	session.setAttribute("seat_no_reverse",seat_no_reverse);
//}
//model.addAttribute("arr_date",arr_date);
//model.addAttribute("dep_date",dep_date);
		
		return view;
	}
	
	//좌석 선택(편도)
	@GetMapping(value="/user_busresv/seat")
	public  void bus_resv_user_seat(Model model, BusBusRoad b,@RequestParam String mode,@RequestParam String one_date,@RequestParam int roadno,@RequestParam int arrival,@RequestParam int departure){
//		Bus_BusRoad seat_b= bs.resv_user_seat_select(roadno);
//		System.out.println("seat_b:"+seat_b);
//		List<BusResv> resv_list=bs.list_seat_resv_user(one_date,roadno);//예약된 좌석 체크위한 리스트
//		String seats="";
//		if(resv_list !=null){
//			for(BusResv r:resv_list){
//				seats +=r.getSeat()+"/";
//			}
//		}else{
//			seats="0";
//		}
//		model.addAttribute("seat_b",seat_b);//자리
//		model.addAttribute("one_date",one_date);
//		model.addAttribute("b",b);
//		model.addAttribute("resv",seats);//예약
//		model.addAttribute("mode",mode);
				
	}

	
//	//배차조회 list
//	@GetMapping(value="/user_busresv/dispatch/{pageNum}/{mode}/{arrival}/{departure}/{one_date}/{arr_date}/{dep_date}/{grade}")
//	public String bus_user_dispatch(@PathVariable("pageNum") String pageNum,@PathVariable("pageNum2") String pageNum2, BusRoad b,HttpSession session, Model model, @PathVariable String mode,@PathVariable int arrival,@PathVariable int departure,@PathVariable String grade,@PathVariable String one_date,@PathVariable String arr_date,@RequestParam String[] seat,@RequestParam String[] seat_reverse,@PathVariable String dep_date){
//		String view = "/user_busresv/dispatch";
//		if(mode.equals("oneway")){//편도 선택 했을떄
//			if(pageNum==null){
//				pageNum="1";
//			}
//			int currentPage = Integer.parseInt(pageNum);
//			int start = currentPage * pageSIZE - (pageSIZE-1);
//			int end = currentPage * pageSIZE;
//			int count = 0;
//			count=bs.bus_busroad_resv_count(arrival,departure,grade);//bus테이블과 bus_road 테이블 조인 시 출발지,도착지,등급과 일치하는 행들 카운트
//			if(end>count){
//				end=count;
//			}
//			List<Bus_BusRoad> dispatch_list=bs.listDispatch_resv_count(arrival,departure,grade,start,end);//출발지,도착지,버스등급에 맞는 리스트를 5개씩출력
//			
	
//			if(grade.equals("전체")){//grade 가 전체 일때
//				currentPage = Integer.parseInt(pageNum);
//				start = currentPage * pageSIZE - (pageSIZE-1);
//				end = currentPage * pageSIZE;
//				count = 0;
//				dispatch_list=bs.listDispatch_resv_all_count(arrival, departure, start, end);//출발지,도착지 에 맞는 리스트를 5개씩 출력
//				count = bs.bus_busroad_resv_all_count(arrival,departure);//bus테이블과 bus_road 테이블 조인 시 출발지,도착지와 일치하는 행들 카운트
//				if(end>count){
//					end=count;
//				}
//			}
//			int startNum = count-((currentPage-1)*pageSIZE);
//			int pageCount = count/pageSIZE + (count%pageSIZE == 0 ? 0 : 1);
//			int pageBlock = 5;
//			int startPage = (currentPage-1)/pageBlock * pageBlock + 1;
//			int endPage = startPage + pageBlock - 1;
//			if (endPage>pageCount) endPage = pageCount;
//			
//			model.addAttribute("mode",mode);
//			model.addAttribute("arrival",arrival);//페이지이동을위해 넘겨줌
//			model.addAttribute("departure",departure);//페이지이동을위해 넘겨줌
//			model.addAttribute("grade",grade);//페이지이동을위해 넘겨줌
//			model.addAttribute("one_date",one_date);
//			model.addAttribute("dispatch_list",dispatch_list);
//			model.addAttribute("count",count);
//			model.addAttribute("startNum",startNum);
//			model.addAttribute("pageCount",pageCount);
//			model.addAttribute("pageBlock",pageBlock);
//			model.addAttribute("startPage",startPage);
//			model.addAttribute("endPage",endPage);
//			
//			}
//			if(mode.equals("twoway")){
//				int pageSize=5;
//				int pageSize2=5;
//				String pageNUM = pageNum;
////				String pageNum2 = pageNum2;
//				
//				if(pageNum==null){
//					pageNum="1";
//				}
//				if(pageNum2==null){
//					pageNum2="1";
//				}
//				int currentPage = Integer.parseInt(pageNum);
//				int start = currentPage * pageSize - (pageSize-1);
//				int end = currentPage * pageSize;
//				int count = 0;
//				
//				int currentPage2 = Integer.parseInt(pageNum2);
//				int start2 = currentPage2 * pageSize2 - (pageSize2-1);
//				int end2 = currentPage2 * pageSize2;
//				int count2 = 0;
//						
//				count =bs.bus_busroad_resv_count(arrival, departure, grade);
//				count2=bs.bus_busroad_resv_resverse_count(arrival, departure,grade);
//				if(end>count){
//					end=count;
//				}
//				if(end>count2){
//					end=count2;
//				}
//				List<Bus_BusRoad> arr_dispatch_list = bs.listDispatch_resv_count(arrival,departure,grade,start,end);//출발지,도착지,버스등급에 맞는 리스트를 5개씩출력
//				List<Bus_BusRoad> dep_dispatch_list = bs.listDispatch_resv_reverse_count(arrival, departure, grade,start2,end2);
//				if(grade.equals("전체")){//grade 가 전체 일떄
//					currentPage = Integer.parseInt(pageNum);
//					start = currentPage * pageSize - (pageSize-1);
//					end = currentPage * pageSize;
//					count = 0;
//							
//					currentPage2 = Integer.parseInt(pageNum2);
//					start2 = currentPage2 * pageSize2 - (pageSize2-1);
//					end2 = currentPage2 * pageSize2;
//					count2=0;
//							
//					arr_dispatch_list = bs.listDispatch_resv_all_count(arrival, departure, start, end);//출발지,도착지 에 맞는 리스트를 5개씩 출력
//					count = bs.bus_busroad_resv_all_count(arrival,departure);//bus테이블과 bus_road 테이블 조인 시 출발지,도착지와 일치하는 행들 카운트
//					
//					//여기 arrival, departure 둘이바뀐건지확인
//					dep_dispatch_list = bs.listDispatch_resv_all_reverse_count(arrival, departure, start2, end2);
//					count2 = bs.bus_busroad_resv_resverse_all_count(arrival, departure);
//				}
//				
//				int startNum = count-((currentPage-1)*pageSize);
//				int pageCount = count/pageSize + (count%pageSize == 0 ? 0 : 1);
//				int pageBlock = 5;
//				int startPage = (currentPage-1)/pageBlock * pageBlock + 1;
//				int endPage = startPage + pageBlock - 1;
//				if (endPage>pageCount) endPage = pageCount;
//				
//				int startNum2 = count2-((currentPage2-1)*pageSize2);
//				int pageCount2 = count2/pageSize2 + (count2%pageSize2 == 0 ? 0 : 1);
//				int pageBlock2 = 5;
//				int startPage2 = (currentPage2-1)/pageBlock2 * pageBlock2 + 1;
//				int endPage2 = startPage2 + pageBlock2 - 1;
//				if (endPage2>pageCount2) endPage2 = pageCount2;
//							
//				if(session.getAttribute("arr_seat_dto")!=null){//왕복 노선 할때 출발일 부터 설정할때 선택한 좌석값 저장
////					String[] seat = seat;
//					int seat_no=seat.length;
//					session.setAttribute("seat",seat);
//					session.setAttribute("seat_no",seat_no);
//				}
//				if(session.getAttribute("dep_seat_dto")!=null){//왕복 노선 할때 도착일 부터 설정할때 선택한 좌석값 저장
////					String[] seat_reverse = seat_reverse;
//					int seat_no_reverse=seat_reverse.length;
//					session.setAttribute("seat_reverse",seat_reverse);
//					session.setAttribute("seat_no_reverse",seat_no_reverse);
//				}
//				model.addAttribute("arr_date",arr_date);
//				model.addAttribute("dep_date",dep_date);
//				model.addAttribute("mode",mode);
//				model.addAttribute("arrival",arrival);
//				model.addAttribute("departure",departure);
//				model.addAttribute("grade",grade);
//				model.addAttribute("arr_dispatch_list",arr_dispatch_list);
//				model.addAttribute("dep_dispatch_list",dep_dispatch_list);
//				model.addAttribute("count",count);
//				model.addAttribute("startNum",startNum);
//				model.addAttribute("pageCount",pageCount);
//				model.addAttribute("pageBlock",pageBlock);
//				model.addAttribute("startPage",startPage);
//				model.addAttribute("endPage",endPage);
//				model.addAttribute("count2",count2);
//				model.addAttribute("startNum2",startNum2);
//				model.addAttribute("pageCount2",pageCount2);
//				model.addAttribute("pageBlock2",pageBlock2);
//				model.addAttribute("startPage2",startPage2);
//				model.addAttribute("endPage2",endPage2);
//						
//			}
//			return view;
//	}
		
}
