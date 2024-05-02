package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusBusRoad;
import com.example.demo.entity.BusResv;
import com.example.demo.entity.BusRoad;
import com.example.demo.entity.BusStation;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Member;
import com.example.demo.entity.Payment;
import com.example.demo.service.BusService;
import com.example.demo.service.Bus_BusroadService;
import com.example.demo.service.BusresvService;
import com.example.demo.service.BusroadService;
import com.example.demo.service.BusstationService;
import com.example.demo.service.MemberService;
import com.example.demo.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
public class BusresvController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;
	public int pageGROUP = 5;
	
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
	
	@Autowired
	private MemberService ms;
	
	@Autowired
	private PaymentService ps;
	
	@GetMapping("/user_busresv/payment")
	@ResponseBody
	public String payment(Payment p) {
		ps.insert(p);
		return "OK";
	}
	
	@GetMapping("/busresv/list/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM) {
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = bs.count();
		
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;
		int startNum = totalRecord-((pageNUM-1)*pageSIZE);
		int pageCount = totalRecord/pageSIZE + (totalRecord%pageSIZE == 0 ? 0 : 1);
		pageGROUP = 5;
		int startPage = (pageNUM-1)/pageGROUP * pageGROUP + 1;
		int endPage = startPage + pageGROUP - 1;
		if (endPage>pageCount) endPage = pageCount;
		
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		
		List<BusResv> list = bs.busresv_list_count(start, end);

		model.addAttribute("list", list); 
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalRecord", totalRecord);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("startNum", startNum);
		model.addAttribute("pageGROUP", pageGROUP);
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
			model.addAttribute("arr_b",arrb);
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
	public String bus_user_dispatch(BusRoad b,HttpSession session,HttpServletRequest req, Model model,@RequestParam String mode,@RequestParam int arrival,@RequestParam int departure,@RequestParam String grade,
			@RequestParam String one_date,@RequestParam String arr_date,@RequestParam String dep_date) {
		String view = "/user_busresv/dispatch";
		if(mode.equals("oneway")){//편도 선택 했을떄
			System.out.println("편도선택");

			List<Map<String, Object>> dispatch_list = bs.list_seat_resv_user(arrival, departure, grade);
			System.out.println(dispatch_list.size());

			for (Map<String, Object> obj : dispatch_list) {
			    System.out.println("obj: " + obj.toString());
			    
			    String id = (String) obj.get("id");
			    int busno = (int) obj.get("busno");
			    int price = (int) obj.get("price");
			    
			    // 가져온 데이터 출력하기
			    System.out.println("ID: " + id);
			    System.out.println("Bus Number: " + busno);
			    System.out.println("Price: " + price);
			}
			
			if(grade.equals("전체")){//grade 가 전체 일때
				dispatch_list = bs.listDispatch_resv_all(arrival, departure);//출발지,도착지에 맞는 리스트
			}
			model.addAttribute("dispatch_list", dispatch_list); 
			model.addAttribute("mode",mode);
			model.addAttribute("arrival",arrival);//페이지이동을위해 넘겨줌
			model.addAttribute("departure",departure);//페이지이동을위해 넘겨줌
			model.addAttribute("grade",grade);//페이지이동을위해 넘겨줌
			model.addAttribute("one_date",one_date);
		}
		
		if(mode.equals("twoway")){
			List<Map<String, Object>> arr_dispatch_list = bs.list_seat_resv_user(arrival,departure,grade);//출발지,도착지,버스등급에 맞는 리스트를 5개씩출력
			List<Map<String, Object>> dep_dispatch_list = bs.listDispatch_resv_reverse_count1(departure, arrival, grade);
			
			if(grade.equals("전체")){//grade가 전체 일떄
				arr_dispatch_list = bs.listDispatch_resv_all(arrival, departure);//출발지,도착지에 맞는 리스트를 5개씩 출력
				dep_dispatch_list = bs.listDispatch_resv_all_reverse(departure, arrival);
			}
			if(session.getAttribute("arr_seat_b")!=null){//왕복 노선 할때 출발일부터 설정할때 선택한 좌석값 저장
				String[] seat = req.getParameterValues("seat");
				System.out.println("seat_length:"+seat.length);
				int seat_no = seat.length;
				session.setAttribute("seat",seat);
				session.setAttribute("seat_no",seat_no);
				int seat_no1 = (int) session.getAttribute("seat_no");
				System.out.println("실험"+seat_no1);
			}
			if(session.getAttribute("dep_seat_b")!=null){//왕복 노선 할때 도착일 부터 설정할때 선택한 좌석값 저장
				String[] seat_reverse=req.getParameterValues("seat_reverse");
				System.out.println("seat_reverse_length:"+seat_reverse.length);
				int seat_no_reverse=seat_reverse.length;
				session.setAttribute("seat_reverse",seat_reverse);
				session.setAttribute("seat_no_reverse",seat_no_reverse);
			}
			model.addAttribute("arr_dispatch_list",arr_dispatch_list);
			model.addAttribute("dep_dispatch_list",dep_dispatch_list);
			model.addAttribute("mode",mode);
			model.addAttribute("arrival",arrival);//페이지이동을위해 넘겨줌
			model.addAttribute("departure",departure);//페이지이동을위해 넘겨줌
			model.addAttribute("grade",grade);//페이지이동을위해 넘겨줌
			model.addAttribute("arr_date",arr_date);
			model.addAttribute("dep_date",dep_date);
			
			String[] seat = (String[]) session.getAttribute("seat"); //로그인되어있는 회원 정보 불러오기  
			String[] seat_reverse = (String[]) session.getAttribute("seat_reverse"); //로그인되어있는 회원 정보 불러오기  
//			int seat_no = (int) session.getAttribute("seat_no"); //null이라고나옴  
//			int seat_no_reverse = (int) session.getAttribute("seat_no_reverse"); //로그인되어있는 회원 정보 불러오기  
			model.addAttribute("seat",seat);
			model.addAttribute("seat_reverse",seat_reverse);
//			model.addAttribute("seat_no",seat_no);
//			model.addAttribute("seat_no_reverse",seat_no_reverse);
		}
		
		return view;
	}
	
	//좌석 선택(편도)
	@GetMapping(value="/user_busresv/seat")
	public String bus_resv_user_seat(HttpServletRequest req, Model model, BusBusRoad b,@RequestParam String mode,@RequestParam String one_date,@RequestParam int roadno,@RequestParam int arrival,@RequestParam int departure){
		String view="/user_busresv/seat";
		System.out.println("busresv/seat발동!");
		Map<String, Object> seat_b= bs.resv_user_seat_select(roadno);
		System.out.println("seat_b:"+seat_b);
		System.out.println((String)seat_b.get("id"));
		List<BusResv> resv_list = bs.list_seat_resv_user(one_date,roadno);//예약된 좌석 체크위한 리스트
		String seats="0";
		System.out.println("resv_list"+resv_list);
		if(resv_list !=null){
			for(BusResv r:resv_list){
				seats +=r.getSeat()+"/";
			}
		}
		System.out.println("seats:"+seats);
		String resvlist[] =seats.split("/");
		System.out.println("resvlist[0]:"+resvlist[0]);
		
		boolean temp = false;

		BusStation bs_a = bss.getBusStation_no(arrival);
		BusStation bs_b = bss.getBusStation_no(departure);
		model.addAttribute("seat_b",seat_b);//자리
		model.addAttribute("one_date",one_date);
		model.addAttribute("b",b);
		model.addAttribute("seats",seats);//예약
		model.addAttribute("mode",mode);
		model.addAttribute("temp",temp);
		model.addAttribute("bs_a",bs_a);
		model.addAttribute("bs_b",bs_b);
		model.addAttribute("resvlist",resvlist);
		
		return view;
	}

	//좌석선택(왕복-가는날)
	@GetMapping(value="/user_busresv/arr_seat")
	public void bus_resv_user_arr_seat(HttpServletRequest req,HttpSession session, Model model, BusBusRoad b,
			@RequestParam String mode,@RequestParam int roadno,@RequestParam String arr_date,@RequestParam String dep_date,@RequestParam int departure,@RequestParam int arrival,@RequestParam String grade){
		
		System.out.println("busresv/arr_seat발동!");
		Map<String, Object> arr_seat_b= bs.resv_user_seat_select(roadno);
		System.out.println("arr_seat_b:"+arr_seat_b);
		System.out.println("seat:"+(int)arr_seat_b.get("seat"));
		int seat = (int)arr_seat_b.get("seat");
		model.addAttribute("seat",seat);
		List<BusResv> resv_list = bs.list_seat_resv_user(arr_date,roadno);//예약된 좌석 체크위한 리스트
		String seats="0";
		System.out.println("resv_list"+resv_list);
		if(resv_list !=null){
			for(BusResv r:resv_list){
				seats +=r.getSeat()+"/";
			}
		}
		System.out.println("seats:"+seats);
		String resvlist[] =seats.split("/");
		System.out.println("resvlist[0]:"+resvlist[0]);
		
		boolean temp = false;
		
		BusStation bs_a = bss.getBusStation_no(arrival);
		BusStation bs_b = bss.getBusStation_no(departure);
		session.setAttribute("arr_seat_b",arr_seat_b);//자리
		session.setAttribute("arr_date",arr_date);
		session.setAttribute("dep_date",dep_date);
		session.setAttribute("seats",seats);//예약
		session.setAttribute("arrival",arrival);
		session.setAttribute("departure",departure);
		session.setAttribute("grade",grade);
		session.setAttribute("roadno",roadno);
		model.addAttribute("arr_seat_b",arr_seat_b);//자리
		model.addAttribute("arr_date",arr_date);
		model.addAttribute("dep_date",dep_date);
		model.addAttribute("seats",seats);//예약
		model.addAttribute("arrival",arrival);
		model.addAttribute("departure",departure);
		model.addAttribute("grade",grade);
		model.addAttribute("roadno",roadno);
		model.addAttribute("b",b);
		model.addAttribute("mode",mode);
		model.addAttribute("temp",temp);
		model.addAttribute("bs_a",bs_a);
		model.addAttribute("bs_b",bs_b);
		model.addAttribute("resvlist",resvlist);
		
	}
	//좌석선택(왕복-오는날)
	@GetMapping(value="/user_busresv/dep_seat")
	public void bus_resv_user_dep_seat(HttpServletRequest req,HttpSession session, Model model, BusBusRoad b,
			@RequestParam String mode,@RequestParam int roadno,@RequestParam String arr_date,@RequestParam String dep_date,@RequestParam int departure,@RequestParam int arrival,@RequestParam String grade){
		
		System.out.println("busresv/dep_seat발동!");
		Map<String, Object> dep_seat_b= bs.resv_user_seat_select(roadno);
		System.out.println("dep_seat_b:"+dep_seat_b);
		System.out.println("seat:"+(int)dep_seat_b.get("seat"));
		int seat = (int)dep_seat_b.get("seat");
		model.addAttribute("seat",seat);
		List<BusResv> resv_list = bs.list_seat_resv_user(dep_date,roadno);//예약된 좌석 체크위한 리스트
		String seats="0";
		System.out.println("resv_list"+resv_list);
		if(resv_list !=null){
			for(BusResv r:resv_list){
				seats +=r.getSeat()+"/";
			}
		}
		System.out.println("seats:"+seats);
		String resvlist[] =seats.split("/");
		System.out.println("resvlist[0]:"+resvlist[0]);
		
		boolean temp = false;

		BusStation bs_a = bss.getBusStation_no(arrival);
		BusStation bs_b = bss.getBusStation_no(departure);
		session.setAttribute("dep_seat_b",dep_seat_b);//자리
		session.setAttribute("arr_date",arr_date);
		session.setAttribute("dep_date",dep_date);
		session.setAttribute("seats",seats);//예약
		session.setAttribute("arrival",arrival);
		session.setAttribute("departure",departure);
		session.setAttribute("grade",grade);
		session.setAttribute("roadno_reverse",roadno);
		model.addAttribute("dep_seat_b",dep_seat_b);//자리
		model.addAttribute("arr_date",arr_date);
		model.addAttribute("dep_date",dep_date);
		model.addAttribute("seats",seats);//예약
		model.addAttribute("arrival",arrival);
		model.addAttribute("departure",departure);
		model.addAttribute("grade",grade);
		model.addAttribute("roadno_reverse",roadno);
		model.addAttribute("b",b);
		model.addAttribute("mode",mode);
		model.addAttribute("temp",temp);
		model.addAttribute("bs_a",bs_a);
		model.addAttribute("bs_b",bs_b);
		model.addAttribute("resvlist",resvlist);
	}
			

	//결제 페이지(편도)
	@GetMapping(value="/user_busresv/pay")
	public String busResvUserPay(HttpServletRequest req, Model model, HttpSession session, BusBusRoad b, @RequestParam String one_date, @RequestParam int roadno, @RequestParam int arrival, @RequestParam int departure) {
	    String view = "/user_busresv/pay";
	    BusStation bs_a = bss.getBusStation_no(arrival);
		BusStation bs_b = bss.getBusStation_no(departure);
	    Map<String, Object> resvb = bs.resv_user_seat_select(roadno);
	    // 예약된 좌석 수를 계산합니다.
	    String[] seat = req.getParameterValues("seat");//좌석수 배열에 저장
	    String[] seats=new String[seat.length];//좌석수를 for문 돌릴 setter 메소드 저장 용도
	    int seat_no=seat.length;//좌석수 저장,티켓총가격 구하기위해

	    int price = (int) resvb.get("price");
	    int save_point = (int) (price * seat_no * 0.1);
	    
	    Member m = (Member) session.getAttribute("user"); //로그인되어있는 회원 정보 불러오기  
	    int mypoint = m.getPoint();
	    model.addAttribute("mypoint", mypoint);
	    model.addAttribute("save_point", save_point);
	    model.addAttribute("resv_b", resvb);
	    model.addAttribute("b", b);
	    model.addAttribute("one_date", one_date);
	    model.addAttribute("seat_no", seat_no);
	    model.addAttribute("seat", seat);
	    model.addAttribute("seatlist", seat);
	    model.addAttribute("roadno", roadno);
	    model.addAttribute("bs_a",bs_a);
		model.addAttribute("bs_b",bs_b);
	    
	    return view;
	}


	@GetMapping(value="/user_busresv/totpay")
	public String bus_resv_user_total_pay(Model model, HttpServletRequest req, HttpSession session, BusBusRoad b, @RequestParam int roadno, @RequestParam int arrival, @RequestParam int departure){
	    String view ="";
	    BusStation bs_a = bss.getBusStation_no(arrival);
	    BusStation bs_b = bss.getBusStation_no(departure);

	    Map<String, Object> depb = (Map<String, Object>) session.getAttribute("dep_seat_b");
	    Map<String, Object> arrb = (Map<String, Object>) session.getAttribute("arr_seat_b");
	    
	    String arr_date = (String) session.getAttribute("arr_date");  
	    String dep_date = (String) session.getAttribute("dep_date");  
	    int roadno_reverse = (int) session.getAttribute("roadno_reverse"); 
	    int arrtime = (int)arrb.get("arrtime");
	    int tottime = (int)arrb.get("tottime");
	    int price = (int)arrb.get("price");
	    int arrtime1 = (int)depb.get("arrtime");
	    int tottime1 = (int)depb.get("tottime");
	    int price1 = (int)depb.get("price");
	    
	    if(depb==null || arrb==null){
	    	view="/user_busresv/dispatch";
	    }else{
	    	if(session.getAttribute("seat_no")!=null){//출발일부터 할떄 조건
				String[] seat_reverse = req.getParameterValues("seat_reverse");
				int seat_no_reverse = seat_reverse.length;
				System.out.println("seat_no_reverse"+seat_no_reverse);
				session.setAttribute("seat_reverse",seat_reverse);
				session.setAttribute("seat_no_reverse",seat_no_reverse);
				model.addAttribute("seat_reverse",seat_reverse);
				model.addAttribute("seat_no_reverse",seat_no_reverse);
				view="/user_busresv/totpay";
			
	    	}else if(session.getAttribute("seat_no_reverse")!=null){//도착일부터 할떄 조건
				String[] seat=req.getParameterValues("seat");
				int seat_no=seat.length;
				System.out.println("seat_no"+seat_no);
				session.setAttribute("seat",seat);
				session.setAttribute("seat_no",seat_no);
				model.addAttribute("seat",seat);
				model.addAttribute("seat_no",seat_no);
				
				view="/user_busresv/totpay";
			}
	    	Member m = (Member) session.getAttribute("user"); //로그인되어있는 회원 정보 불러오기  
		    int mypoint = m.getPoint();
		    model.addAttribute("mypoint", mypoint);
		    model.addAttribute("b", b);
		    model.addAttribute("roadno", roadno);
		    model.addAttribute("bs_a",bs_a);
			model.addAttribute("bs_b",bs_b);
			model.addAttribute("arr_seat_b",arrb);
			model.addAttribute("dep_seat_b",depb);
			model.addAttribute("arr_date",arr_date);
			model.addAttribute("dep_date",dep_date);
			model.addAttribute("roadno_reverse",roadno_reverse);
			model.addAttribute("arrtime",arrtime);
			model.addAttribute("arrtime1",arrtime1);
			model.addAttribute("tottime",tottime);
			model.addAttribute("tottime1",tottime1);
			model.addAttribute("price",price);
			model.addAttribute("price1",price1);
			
	    }
	    return view;
	}

	
	//결제완료 (편도 bus_resv 테이블에 저장)
	@GetMapping(value="/user_busresv/payok")
	public void bus_resv_user_payok(BusResv b, Model model,@RequestParam String one_date,HttpServletRequest req, HttpSession session, @RequestParam int roadno
			,@RequestParam int save_point,@RequestParam int updatetot, @RequestParam int usepoint){
		Member m = (Member) session.getAttribute("user"); //로그인되어있는 회원 정보 불러오기    
		Map<String, Object> rdto = bs.resv_user_seat_select(roadno);

		int total = 0;
		String name = "";	
		if(name.equals("")) {
			BusStation bs_a = bss.getBusStation_no((int)rdto.get("arrival"));
			BusStation bs_b = bss.getBusStation_no((int)rdto.get("departure"));
			name = bs_a.getStationname()+"▶ "+ bs_b.getStationname()+" 외 0건";
		}
		String[] seat = req.getParameterValues("seat");//좌석수 배열에 저장
		String[] seats=new String[seat.length];//좌석수를 for문 돌릴 setter 메소드 저장 용도
		int seat_no = seat.length;//좌석수 저장,티켓총가격 구하기위해

		//좌석번호 구하기
		for(int i=0; i<seat.length; i++){
			b.setSeat(seat[i]);
			seats[i]=b.getSeat();
		}

		String result_seat = String.join("/",seats); //seats 배열의 자리 번호를 '/' 기준으로 나누어서 저장
		System.out.println("updatetot:"+updatetot);
		if(usepoint ==0){ // 포인트를 사용하지 않았을떄
			b.setUsepoint(0);
			b.setSavepoint(save_point);
			b.setPrice(updatetot);
			m.setPoint(m.getPoint()+b.getSavepoint());
			ms.update(m); 
		}else{ //포인트를 사용했을떄(포인트적립x)
			b.setUsepoint(usepoint);
			b.setSavepoint(0);
			b.setPrice(updatetot-b.getUsepoint());
			m.setPoint(m.getPoint()-b.getUsepoint());
			ms.update(m);
		}
		b.setBusno((int)rdto.get("busno"));
		b.setResvdate(one_date);
		b.setRoadno(roadno);
		b.setSeat(result_seat);
		b.setId(m.getId());
		
		bs.insert(b);
		
		model.addAttribute("rdto",rdto);
		model.addAttribute("seat_no",seat_no);
		model.addAttribute("use_point",usepoint);
		model.addAttribute("updatetot",updatetot);
		model.addAttribute("name",name);
	}
	
	//결제완료 (왕복 bus_resv 테이블에 저장)
	@GetMapping(value="/user_busresv/totpayok")
	public void bus_resv_user_total_payok(BusResv b, Model model,@RequestParam String arr_date, @RequestParam String dep_date, HttpServletRequest req, HttpSession session, @RequestParam int roadno
			,@RequestParam int roadno_reverse,@RequestParam int save_point,@RequestParam int updatetot, @RequestParam int usepoint, @RequestParam int arr_price,@RequestParam int dep_price){
		Member m = (Member) session.getAttribute("user"); //로그인되어있는 회원 정보 불러오기    
		Map<String, Object> rdto = bs.resv_user_seat_select(roadno);
		Map<String, Object> rdto_reverse = bs.resv_user_seat_select(roadno_reverse);
		
		int total = 0;
		String name = "";	
		if(name.equals("")) {
			BusStation bs_a = bss.getBusStation_no((int)rdto.get("arrival"));
			BusStation bs_b = bss.getBusStation_no((int)rdto.get("departure"));
			name = bs_a.getStationname()+"▶ "+ bs_b.getStationname()+" 외 1건";
		}
		String[] seat = req.getParameterValues("seat");//좌석수 배열에 저장
		String[] seats = new String[seat.length];//좌석수를 for문 돌릴 setter 메소드 저장 용도
		int seat_no = seat.length;//좌석수 저장,티켓총가격 구하기위해
		
		String[] seat_reverse = req.getParameterValues("seat_reverse");//좌석수 배열에 저장
		String[] seats_reverse=new String[seat_reverse.length];//좌석수를 for문 돌릴 setter 메소드 저장 용도
		int seat_no_reverse=seat_reverse.length;//좌석수 저장,티켓총가격 구하기위해

		//좌석번호 구하기
		for(int i=0; i<seat.length; i++){
			b.setSeat(seat[i]);
			seats[i]=b.getSeat();
		}
		String result_seat = String.join("/",seats); //seats 배열의 자리 번호를 '/' 기준으로 나누어서 저장
		
		//좌석번호 구하기
		for(int j=0; j<seat_reverse.length; j++){
			b.setSeat(seat_reverse[j]);
			seats_reverse[j]=b.getSeat();
		}
		String result_seat_reverse = String.join("/",seat_reverse); //seats 배열의 자리 번호를 '/' 기준으로 나누어서 저장

		System.out.println("updatetot:"+updatetot);
		if(usepoint ==0){ // 포인트를 사용하지 않았을떄
			b.setUsepoint(0);
			b.setSavepoint(save_point);
			b.setPrice(updatetot);
			m.setPoint(m.getPoint()+b.getSavepoint());
			ms.update(m); 
		}else{ //포인트를 사용했을떄(포인트적립x)
			b.setUsepoint(usepoint);
			b.setSavepoint(0);
			b.setPrice(updatetot-b.getUsepoint());
			m.setPoint(m.getPoint()-b.getUsepoint());
			ms.update(m);
		}
		b.setUsepoint(0);
		b.setSavepoint(0);
		b.setPrice(arr_price);
		b.setBusno((int)rdto.get("busno"));
		b.setResvdate(dep_date);
		b.setRoadno(roadno);
		b.setSeat(result_seat);
		b.setId(m.getId());
		
		bs.insert(b);
		
		b.setUsepoint(0);
		b.setSavepoint(0);
		b.setPrice(dep_price);
		b.setBusno((int)rdto_reverse.get("busno"));
		b.setResvdate(dep_date);
		b.setRoadno(roadno_reverse);
		b.setSeat(result_seat_reverse);
		b.setId(m.getId());
		
		bs.insert(b);
		
		model.addAttribute("price",arr_price+dep_price);
		
		session.removeAttribute("arr_seat_b");
		session.removeAttribute("dep_session_b");
		session.removeAttribute("seat");
		session.removeAttribute("seat_reverse");
		
		model.addAttribute("rdto",rdto);
		model.addAttribute("qty",seat_no+seat_no_reverse);
		model.addAttribute("use_point",usepoint);
		model.addAttribute("updatetot",updatetot);
		model.addAttribute("name",name);

	}
	//예약리스트 페이지
	@GetMapping(value="/user_busresv/resvlist")
	public void bus_resv_user_resvlist(HttpSession session, Model model){
		String msg="";
		String url="";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
		}else{
			Member m = (Member) session.getAttribute("user");
			List<Map<String, Object>> resv_list = bs.resvlist(m.getId());
			model.addAttribute("resv_list",resv_list);
		}
	}
	
	//환불하기
	@GetMapping("/user_busresv/refund/{resvno}")
	public String bus_resv_user_refund(Model model, @PathVariable("resvno") int resvno) {
		String view = "message";
		bs.delete(resvno);
		String msg = "환불성공";
		String url = "/user_busresv/resvlist";
		model.addAttribute("msg",msg);
		model.addAttribute("url",url);
		return view;
	}
	
}
