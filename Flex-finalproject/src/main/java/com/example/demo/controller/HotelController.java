package com.example.demo.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.dao.HotelDAO;
import com.example.demo.dao.RoomDAO;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.HotelResv;
import com.example.demo.entity.Member;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Room;
import com.example.demo.service.HotelResvService;
import com.example.demo.service.HotelService;
import com.example.demo.service.MemberService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.RoomService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
public class HotelController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;
	public int pageGROUP = 5;


	@Autowired
	private HotelService hs;

	@Autowired
	private HotelResvService hrs;
	
	@Autowired
	private RoomService rs;

	@Autowired
	private MemberService ms;

	@Autowired
	private HotelDAO dao;

	@Autowired
	private RoomDAO roomdao;

	@Autowired
	private PaymentService ps;
	
	private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

	//호텔
	@GetMapping("/hotel/search")
	public void hotelsearch(){
	}

	@GetMapping("/hotelcheck")
	public String hotelcheck(){
		return "/hotel/hotelmain";
	}

	@GetMapping("/hotel/insert")
	public void insertForm(@RequestParam(value = "resvno", defaultValue = "0")int resvno, Model model) {		
		model.addAttribute("resvno", resvno);
	}

	@PostMapping("/hotel/insert")
	public String insertSubmit(Hotel h, MultipartHttpServletRequest req) {
		String view = "redirect:/hotel/list/1";
		String path = req.getServletContext().getRealPath("/images/hotels");
		System.out.println("path:" + path);
		// 업로드된 파일들을 가져옵니다.
		List<MultipartFile> fileList = req.getFiles("uploadFile");
		String filename = "";

		// 각 파일에 대해 처리를 수행합니다.
		for (MultipartFile mf : fileList) {
			String fname = mf.getOriginalFilename();

			// 업로드된 파일이 있는 경우에만 처리합니다.
			if (!fname.isEmpty()) {
				try {
					// 파일을 저장합니다.
					mf.transferTo(new File(path + "/" + fname));
					filename+=fname+"/";

					h.setFilename(filename);
				} catch (Exception e) {
					System.out.println("예외발생: " + e.getMessage());
				}
			}
		}
		hs.insert(h);
		return view;
	}

	@GetMapping("/hotel/list/{pageNUM}")
	public String list(Model model, HttpSession session, @PathVariable("pageNUM") int pageNUM, @RequestParam(value="search", required=false) String search, @RequestParam(value="searchString", required=false) String searchString,
			@RequestParam(value="startresvdate", required=false) String startresvdate, @RequestParam(value="endresvdate", required=false) String endresvdate	) {
		List<Hotel> list;
		String view = "/hotel/list";
		String msg="";
		String url="";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
		
		System.out.println("pageNUM:"+pageNUM);
		pageSIZE = 6;
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);

		int start = 0;
		int end = 0;
		if (search == null) {
			search = "all";
		}
		if (search.equals("hname")) {
			session.setAttribute("search", search);
			totalRecord = hs.find_hotel_count(searchString);
			totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
			start = (pageNUM-1)*pageSIZE+1;
			end = start+pageSIZE-1;
            list = hs.find_hotel(searchString, start, end);
            String[] filenames = new String[list.size()];
    		String firstfilename="";
    		//호텔 리스트 메인 사진 한장 가져오기
    		int index = 0;
    		for(Hotel hotel : list){
    			int hotelno = hotel.getHotelno();
    			System.out.println("hotelno"+hotelno);
    			String filename= hotel.getFilename();
    			String[] arrfilename = filename.split("/");
    			System.out.println(arrfilename[0]);
    			firstfilename = arrfilename[0];
    			filenames[index] = firstfilename;
    			index++;
    		}
    		model.addAttribute("filenames", filenames);
        } else {
        	totalRecord = hs.count(); //count는 기본적으로제공해줘
    		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
    		start = (pageNUM-1)*pageSIZE+1;
    		end = start+pageSIZE-1;
        	list = hs.hotel_list_count(start, end);
        	String[] filenames = new String[list.size()];
    		String firstfilename="";
    		//호텔 리스트 메인 사진 한장 가져오기
    		int index = 0;
    		for(Hotel hotel : list){
    			int hotelno = hotel.getHotelno();
    			System.out.println("hotelno"+hotelno);
    			String filename= hotel.getFilename();
    			String[] arrfilename = filename.split("/");
    			System.out.println(arrfilename[0]);
    			firstfilename = arrfilename[0];
    			filenames[index] = firstfilename;
    			index++;
    		}
    		model.addAttribute("filenames", filenames);
        }
		int startNum = totalRecord-((pageNUM-1)*pageSIZE);
		int pageCount = totalRecord/pageSIZE + (totalRecord%pageSIZE == 0 ? 0 : 1);
		pageGROUP = 5;
		int startPage = (pageNUM-1)/pageGROUP * pageGROUP + 1;
		int endPage = startPage + pageGROUP - 1;
		if (endPage>pageCount) endPage = pageCount;

		System.out.println("start:"+start);
		System.out.println("end:"+end);
		System.out.println("pageGROUP:"+pageGROUP);
		System.out.println("startPage:"+startPage);
		
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalRecord", totalRecord);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("startNum", startNum);
		model.addAttribute("pageGROUP", pageGROUP);
		System.out.println("pageNUM:"+pageNUM);
		
	    model.addAttribute("list", list);
	    model.addAttribute("search", search);
	    model.addAttribute("searchString", searchString);
	    model.addAttribute("startresvdate", startresvdate);
	    model.addAttribute("endresvdate", endresvdate);
	    return view; 
		}
	}
	

	@GetMapping("/hotel/update/{hotelno}")
	public String hotel_update(@PathVariable int hotelno, Model model) {
		model.addAttribute("hotel", hs.getHotel(hotelno));
		return "/hotel/update";
	}

	@PostMapping("/hotel/update")
	public String hotel_updateSubmit(Hotel h, MultipartHttpServletRequest req, Model model, @RequestParam(value = "id") String id) {
		String view = "redirect:/hotel/list/1";
		String path = req.getServletContext().getRealPath("/images/hotels"); // 이미지 저장 위치
		System.out.println("path:" + path);

		List<MultipartFile> fileList = req.getFiles("uploadFile"); // 업로드된 파일 목록을 가져옵니다.

		// 새로운 파일 이름을 저장할 변수입니다.
		String filename = "";
		filename = h.getFilename();
		h.setFilename(filename);
		filename = "";
		// 각 파일에 대해 처리를 수행합니다.
		for (MultipartFile mf : fileList) {
			String fname = mf.getOriginalFilename(); // 파일 이름을 가져옵니다.
			System.out.println("fname: " + fname);

			// 업로드된 파일이 있는 경우에만 처리합니다.
			if (!fname.isEmpty()) {
				try {

					// 파일을 저장합니다.
					mf.transferTo(new File(path + "/" + fname));

					// 새 파일 이름을 filename에 추가합니다.
					filename += fname + "/";
					// 파일 이름을 업데이트된 파일 이름으로 설정합니다.
					h.setFilename(filename);
				} catch (Exception e) {
					System.out.println("예외발생: " + e.getMessage());
				}
			}
		}
		// 호텔 ID를 설정합니다.
		h.setId(id);

		// 호텔을 업데이트합니다.
		hs.update(h);

		return view;
	}

	@GetMapping("/hotel/hotelcontent/{hotelno}")
	public String hotelcontent(@PathVariable("hotelno") int hotelno, Model model){

		String view = "/hotel/hotelcontent";
		Hotel h = hs.getHotel(hotelno);
		String id = h.getId();
		Member m = ms.getMember(id);
		String name = m.getName();

		//filename split사용해서 끊어서 보내기
		String hotelfile = h.getFilename();
		String [] filearr = hotelfile.split("/");
		for (String item : filearr) {
			System.out.println(item);
		}
		model.addAttribute("filearr", filearr);

		model.addAttribute("hoteloner", name);
		model.addAttribute("h", h);

		return view;
	}

	@GetMapping("/hotel/delete/{hotelno}")
	public String delete(@PathVariable("hotelno") int hotelno, HttpServletRequest request) {
		String view = "redirect:/hotel/list/1";
		String path = request.getServletContext().getRealPath("/images/hotels");
		String oldFname = hs.getHotel(hotelno).getFilename();
		System.out.println(oldFname);
		hs.delete(hotelno);
		try {
			File file = new File(path +"/"+oldFname);
			file.delete();
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return view;
	}

	//-------------------room---------------------

	@GetMapping("/room/insert/{hotelno}")
	public String roominsert(@PathVariable("hotelno") int hotelno, Model model){
		model.addAttribute("hotelno", hotelno);
		return "/room/insert";
	}

	@PostMapping("/room/insert/{hotelno}")
	public String roominsertok(Model model, Room r, MultipartHttpServletRequest req, @PathVariable(value = "hotelno") int hotelno){
		System.out.println(hotelno);
		String view = "redirect:/room/list/1";
		String path = req.getServletContext().getRealPath("/images/room");
		System.out.println("path:" + path);
		// 업로드된 파일들을 가져옵니다.
		List<MultipartFile> fileList = req.getFiles("uploadFile");
		String filename = "";

		// 각 파일에 대해 처리를 수행합니다.
		for (MultipartFile mf : fileList) {
			String fname = mf.getOriginalFilename();

			// 업로드된 파일이 있는 경우에만 처리합니다.
			if (!fname.isEmpty()) {
				try {
					// 파일을 저장합니다.
					mf.transferTo(new File(path + "/" + fname));
					filename+=fname+"/";

				} catch (Exception e) {
					System.out.println("예외발생: " + e.getMessage());
				}
			}
		}

		String maxRoomno = roomdao.maxRoomno(hotelno); // 가장 큰 방 번호 가져오기
		int lastNumber = 0;
		if (!maxRoomno.equals("0") && maxRoomno.contains("-")) {
			lastNumber = Integer.parseInt(maxRoomno.substring(maxRoomno.lastIndexOf("-") + 1));
		}
		for (int i = 1; i <= r.getRooms(); i++) {
			int newRoomNumber = lastNumber + i; // 새로운 방 번호 생성
			String newRoomno = hotelno + "-" + newRoomNumber; // 새로운 방 번호 생성
			r.setFilename(filename); // 파일명 설정
			r.setRoomno(newRoomno); // 방 번호 설정
			r.setHotelno(hotelno); // 호텔 번호 설정
			rs.insert(r); // 데이터베이스에 방 추가
		}
		if(r.getHotelno()==hotelno){
			List<Room> list = rs.listRoom2(hotelno);
			model.addAttribute("roomList", list);
		}

		model.addAttribute("hotelno", hotelno);
		return view;

	}

	@GetMapping("/room/list/{hotelno}")
	public String roomlist(Model model, @PathVariable(value = "hotelno") int hotelno){
		List<Room> list = rs.listRoom2(hotelno);
		String[] filenames = new String[list.size()];
		String firstfilename="";
		//룸 리스트 메인 사진 한장 가져오기
		int index = 0;
		for(Room room : list){
			String roomno = room.getRoomno();
			String filename= room.getFilename();
			String[] arrfilename = filename.split("/");
			System.out.println(arrfilename[0]);
			firstfilename = arrfilename[0];
			filenames[index] = firstfilename;
			index++;
		}
		model.addAttribute("filenames", filenames);
		model.addAttribute("list", list); 

		return "/room/list";
	}

	@GetMapping("/room/roomcontent/{roomno}")
	public String roomcontent(Model model, @PathVariable("roomno") String roomno){
		Room r = rs.getRoom(roomno);
		int hotelno = r.getHotelno();
		Hotel h = hs.getHotel(hotelno);
		String hotelname = h.getName();
		String hotelinfo = h.getInfo();
		String hoteladdr = h.getAddress();
		//룸 사진 여러장 가져오기
		String roomfile = r.getFilename();
		String regex="/";
		String [] filearr = roomfile.split(regex);

		model.addAttribute("filearr", filearr);
		model.addAttribute("r", r);
		model.addAttribute("hotelno", hotelno);
		model.addAttribute("hotelname", hotelname);
		model.addAttribute("hotelinfo", hotelinfo);
		model.addAttribute("hoteladdr", hoteladdr);
		return "/room/roomcontent";
	}

	//-------------------hotelresv---------------------

	@GetMapping("/hotel_resv/hotel_resv")
	public void hotel_resvselect(Model model, HttpSession session){
		Member user = (Member) session.getAttribute("user");
		model.addAttribute("user",user);
	}

	@GetMapping("/hotel_resv/resvlist")
	public String hotel_resvlist(HttpSession session, Model model, @RequestParam(value = "address") String address,
			@RequestParam(value = "startresvdate") String startresvdate,@RequestParam(value = "endresvdate") String endresvdate,
			@RequestParam(value = "sleeps") int sleeps, @RequestParam(value = "roomsu") int roomsu){
		List<Hotel> list = hs.listHoteladdress(address);
		int arrhotelno[];
		int hotelno=0;
		String[] filenames = new String[list.size()];
		String firstfilename="";

		HashMap<Integer, List<Room>> hlist = new HashMap<Integer, List<Room>>();

		//호텔 리스트 메인 사진 한장 가져오기
		int index = 0;
		for(Hotel hotel : list){
			hotelno = hotel.getHotelno();
			System.out.println("hotelno"+hotelno);
			List<Room> rlist = rs.listRoom2(hotelno);
			if (!rlist.isEmpty()) {
			for (Room room : rlist) {
		        if (room.getSleeps() == sleeps) {
		            System.out.println("Matching sleeps found for hotel " + hotelno);
		            hlist.put(hotelno, rlist);
		            break;
		        }
		    }
			}
			String filename= hotel.getFilename();
			String[] arrfilename = filename.split("/");
			System.out.println(arrfilename[0]);
			firstfilename = arrfilename[0];
			filenames[index] = firstfilename;
			index++;
		}
		model.addAttribute("filenames", filenames);

		// 날짜 포맷 지정
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        Date startDate = sdf1.parse(startresvdate);
	        Date endDate = sdf1.parse(endresvdate);
		     // 시작 날짜의 연, 월, 일 추출
	        Calendar startCal = Calendar.getInstance();
	        startCal.setTime(startDate);
	        int startYear = startCal.get(Calendar.YEAR);
	        int startMonth = startCal.get(Calendar.MONTH);
	        int startDay = startCal.get(Calendar.DAY_OF_MONTH);
	        // 종료 날짜의 연, 월, 일 추출
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            int endYear = endCal.get(Calendar.YEAR);
            int endMonth = endCal.get(Calendar.MONTH);
            int endDay = endCal.get(Calendar.DAY_OF_MONTH);

            // 날짜 차이 계산
            long differenceInMillis = endDate.getTime() - startDate.getTime();
            long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
            System.out.println("differenceInDays:"+differenceInDays);
            model.addAttribute("differenceInDays", differenceInDays);
            session.setAttribute("differenceInDays",differenceInDays);
        }catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
        
		session.setAttribute("startresvdate",startresvdate);
		session.setAttribute("endresvdate",endresvdate);
		model.addAttribute("startresvdate", startresvdate);
		model.addAttribute("endresvdate", endresvdate);
		model.addAttribute("hotelList", list);
		model.addAttribute("roomInfo", hlist);
		model.addAttribute("address", address);
		model.addAttribute("roomsu", roomsu);
		model.addAttribute("sleeps", sleeps);
		return "/hotel_resv/resvlist";
	}

	@GetMapping("/hotelresv/listhotelresv")
	@ResponseBody
	public List<HotelResv> listhotelresv(int hotelno){
		return hrs.ADlistHotelresv(hotelno);
	}

	@GetMapping("/hotel_resv/roomcontent/{hotelno}/{roomno}")
	public String hotel_resvroomcontent(Model model,@PathVariable("hotelno") int hotelno, @PathVariable("roomno") String roomno){
		Hotel h = hs.getHotel(hotelno);
		String hotelname = h.getName();
		String hotelinfo = h.getInfo();
		String hoteladdr = h.getAddress();
		Room r = rs.getRoom(roomno);
		if(r.getFilename() !=null) {
		//filename split사용해서 끊어서 보내기
		String roomfile = r.getFilename();
		String [] filearr = roomfile.split("/");
		for (String item : filearr) {
			System.out.println(item);
		}
		model.addAttribute("filearr", filearr);
		model.addAttribute("r", r);
		model.addAttribute("hotelno", hotelno);
		model.addAttribute("roomno", roomno);
		model.addAttribute("hotelname", hotelname);
		model.addAttribute("hotelinfo", hotelinfo);
		model.addAttribute("hoteladdr", hoteladdr);
		}
		return "/hotel_resv/roomcontent";
	}

	@GetMapping("/hotel_resv/resv/{hotelno}/{roomno}")
	public String hotel_resvcontent(HttpSession session, Model model, @PathVariable("hotelno") int hotelno, @PathVariable("roomno") String roomno){
		String startresvdate = (String)session.getAttribute("startresvdate");
		String endresvdate = (String)session.getAttribute("endresvdate");
		if (startresvdate == null || endresvdate == null) {
			String url = "/hotel_resv/hotel_resv";
			String msg = "죄송합니다 예약날짜가 설정이 안되었습니다.";
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
		    return "message";
		}
		long differenceInDays = (long)session.getAttribute("differenceInDays");
		Room r = rs.getRoom(roomno);
		Hotel h = hs.getHotel(hotelno);

		int price = r.getPrice();
	    int save_point = (int) (price * 0.01);
		Member m = (Member) session.getAttribute("user"); //로그인되어있는 회원 정보 불러오기  
	    int mypoint = m.getPoint();
	    if(m.getMinor()==1) {
	    	String url = "/hotel_resv/hotel_resv";
			String msg = "죄송합니다 미성년자는 예약대상이 아닙니다.";
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
	    	return "message";
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        model.addAttribute("currentDateAndTime", currentDateAndTime);
	    model.addAttribute("mypoint", mypoint);
	    model.addAttribute("price", price);
	    model.addAttribute("save_point", save_point);
	    model.addAttribute("r", r);
	    model.addAttribute("h", h);
	    model.addAttribute("m", m);
		model.addAttribute("hotelno", hotelno);
		model.addAttribute("roomno", roomno);
		model.addAttribute("startresvdate", startresvdate);
		model.addAttribute("endresvdate", endresvdate);
		model.addAttribute("differenceInDays", differenceInDays);
		return "/hotel_resv/resv";
	}
	
	//결제완료 (hotelresv 테이블에 저장)
	@GetMapping(value="/hotel_resv/payok")
	public void bus_resv_user_payok(HotelResv r, Model model, HttpServletRequest req, HttpSession session, @RequestParam int hotelno, @RequestParam String roomno
			,@RequestParam int save_point,@RequestParam int updatetot,@RequestParam int totprice, @RequestParam int usepoint, @RequestParam String resvdate){
		Member m = (Member) session.getAttribute("user"); //로그인되어있는 회원 정보 불러오기    
		System.out.println("payok발동");
		int total = 0;
		String name = "";	
		String startresvdate = (String)session.getAttribute("startresvdate");
		String endresvdate = (String)session.getAttribute("endresvdate");
		if(name.equals("")) {
			name = startresvdate+"▶ "+ endresvdate+" 외 0건";
		}
		System.out.println("updatetot:"+updatetot);
		if(usepoint ==0){ // 포인트를 사용하지 않았을떄
			r.setUsepoint(0);
			r.setSavepoint(save_point);
			r.setPrice(updatetot);
			m.setPoint(m.getPoint()+r.getSavepoint());
			ms.update(m); 
		}else{ //포인트를 사용했을떄(포인트적립x)
			r.setUsepoint(usepoint);
			r.setSavepoint(0);
			r.setPrice(updatetot);
			m.setPoint(m.getPoint()-usepoint);
			ms.update(m);
		}
		r.setHotelno(hotelno);
		r.setRoomno(roomno);
		r.setResvdate(resvdate);
		r.setStartresvdate(startresvdate);
		r.setEndresvdate(endresvdate);
		r.setId(m.getId());
	
		hrs.insert(r);
		int resvno = r.getHotelresvno();
		System.out.println("resvno:"+resvno);
		long differenceInDays = (long)session.getAttribute("differenceInDays");
		model.addAttribute("differenceInDays", differenceInDays);
		model.addAttribute("use_point",usepoint);
		model.addAttribute("updatetot",updatetot);
		model.addAttribute("name",name);
		model.addAttribute("hotelno",hotelno);
		model.addAttribute("roomno",roomno);
		model.addAttribute("totprice",totprice);
		model.addAttribute("resvno",resvno);
	}
	
	@GetMapping("/hotel_resv/payment")
	@ResponseBody
	public String payment(Payment p) {
		ps.insert(p);
		return "OK";
	}

	//예약리스트 페이지
	@GetMapping(value="/hotel_resv/paylist")
	public String hotel_resv_user_resvlist(HttpSession session, Model model){
		String msg="";
		String url="";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
			Member m = (Member) session.getAttribute("user");
			List<Map<String, Object>> pay_list = ps.listPayhotelresvbyId(m.getId());
			model.addAttribute("pay_list",pay_list);
			return "/hotel_resv/paylist";
		}
		
	}
	
	//환불하기
	@GetMapping("/hotel_resv/refund/{no}")
	public String bus_resv_user_refund(HttpSession session,Model model, @PathVariable("no") int no, @RequestParam int usepoint) {
		String view = "message";
		Member m = (Member) session.getAttribute("user");
		List<Map<String, Object>> pay_list = ps.listPayhotelresvbyId(m.getId());
		for (Map<String, Object> obj : pay_list) {
		    System.out.println("obj: " + obj.toString());
		    String id = (String) obj.get("id");
		    if(m.getId().equals(id)) {
		    	ps.delete(no);
		    	m.setPoint(m.getPoint()+usepoint);
		    }
		}
		String msg = "환불 완료, 사용포인트: " + usepoint+"점 환불 완료";
		String url = "/hotel_resv/paylist";
		model.addAttribute("msg",msg);
		model.addAttribute("url",url);
		return view;
	}
}
