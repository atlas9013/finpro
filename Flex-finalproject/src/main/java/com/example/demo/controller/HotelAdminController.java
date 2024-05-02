package com.example.demo.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.dao.HotelBoardDAO;
import com.example.demo.dao.HotelResvDAO;
import com.example.demo.dao.RoomDAO;
import com.example.demo.entity.BusStation;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.HotelResv;
import com.example.demo.entity.Hotelboard;
import com.example.demo.entity.Member;
import com.example.demo.entity.Room;
import com.example.demo.service.HotelBoardService;
import com.example.demo.service.HotelResvService;
import com.example.demo.service.HotelService;
import com.example.demo.service.MemberService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.RoomService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
public class HotelAdminController {

	public int pageSIZE = 5;
	public int totalRecord = 0;
	public int totalPage = 1;
	public int pageGROUP = 5;

	@Autowired
	private HotelService hs;

	@Autowired
	private RoomService rs;

	@Autowired
	private HotelResvService hrs;

	@Autowired
	private HotelBoardService hbs;
	
	@Autowired
	private MemberService ms;
	
	@Autowired
	private PaymentService ps;
	
	@Autowired
	private HotelBoardDAO hotelboarddao;
	
	@Autowired
	private HotelResvDAO hotelresvdao;
	
	@Autowired
	private RoomDAO roomdao;

	@GetMapping("/hotelAD/main")
	public String hotelAD(HttpServletRequest req) {
		return "/hotelAD/main";
	}
	@GetMapping("/hotel_board/hotel_list/{pageNUM}")
	public String hotel_list1(Model model, @PathVariable("pageNUM") int pageNUM, @RequestParam(value="search", required=false) String search, @RequestParam(value="searchString", required=false) String searchString) {
		String view = "/hotel_board/hotel_list";
		List<Hotel> list;
		HashMap<Integer, Integer> hotelboardcnt = new HashMap<Integer, Integer>();
		HashMap<Integer, String> hotelboardString = new HashMap<Integer, String>();
		int hotelno = 0;
		int cnt = 0;
		String regdate = "";
		System.out.println("pageNUM:"+pageNUM);
		pageSIZE = 6;
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);

		int start = 0;
		int end = 0;
		if (search == null) {
			search = "all";
		}
		if (search.equals("hname")) {
			totalRecord = hs.find_hotel_count(searchString);
			totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
			start = (pageNUM-1)*pageSIZE+1;
			end = start+pageSIZE-1;
            list = hs.find_hotel(searchString, start, end);
            for(Hotel hotel : list){
            	hotelno = hotel.getHotelno();
            	cnt = hbs.hotelboardcountbyHotelno(hotelno);
            	hotelboardcnt.put(hotelno, cnt);
            }
            
        } else {
        	totalRecord = hs.count(); //count는 기본적으로제공해줘
    		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
    		start = (pageNUM-1)*pageSIZE+1;
    		end = start+pageSIZE-1;
        	list = hs.hotel_list_count(start, end);
        	for(Hotel hotel : list){
            	hotelno = hotel.getHotelno();
            	cnt = hbs.hotelboardcountbyHotelno(hotelno);
            	regdate = hbs.hotelboardregdatebyHotelno(hotelno);
            	hotelboardcnt.put(hotelno, cnt);
            	hotelboardString.put(hotelno, regdate);
            }
        }
		//메인
		String[] filenames = new String[list.size()];
		String firstfilename="";
		int index = 0;
		for(Hotel hotel : list){
			String filename= hotel.getFilename();
			String[] arrfilename = filename.split("/");
			System.out.println(arrfilename[0]);
			firstfilename = arrfilename[0];
			filenames[index] = firstfilename;
			index++;
		}
		model.addAttribute("filenames", filenames);
		
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
	    model.addAttribute("hotelboardcnt", hotelboardcnt);
	    model.addAttribute("hotelboardString", hotelboardString);
		
		return view;
	}
	
	@GetMapping("/hotel_board/hotel_list2/{pageNUM}")
	public String hotel_list2(Model model, @PathVariable("pageNUM") int pageNUM, @RequestParam(value="search", required=false) String search, @RequestParam(value="searchString", required=false) String searchString) {
		String view = "/hotel_board/hotel_list2";
		List<Hotel> list;
		System.out.println("pageNUM:"+pageNUM);
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		
		int start = 0;
		int end = 0;
		if (search == null) {
			search = "all";
		}
		if (search.equals("hname")) {
			totalRecord = hs.find_hotel_count(searchString);
			totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
			start = (pageNUM-1)*pageSIZE+1;
			end = start+pageSIZE-1;
			list = hs.find_hotel(searchString, start, end);
			
		} else {
			totalRecord = hs.count(); //count는 기본적으로제공해줘
			totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
			start = (pageNUM-1)*pageSIZE+1;
			end = start+pageSIZE-1;
			list = hs.hotel_list_count(start, end);
		}
		//메인
		String[] filenames = new String[list.size()];
		String firstfilename="";
		int index = 0;
		for(Hotel hotel : list){
			String filename= hotel.getFilename();
			String[] arrfilename = filename.split("/");
			System.out.println(arrfilename[0]);
			firstfilename = arrfilename[0];
			filenames[index] = firstfilename;
			index++;
		}
		model.addAttribute("filenames", filenames);
		
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
		
		return view;
	}
	
	
	
	@GetMapping("/hotel_board/list/{hotelno}/{pageNUM}")
	public String list(Model model, @PathVariable("pageNUM") int pageNUM, @PathVariable("hotelno") int hotelno) {
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = hbs.hotel_board_count();
		
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
		System.out.println("pageGROUP:"+pageGROUP);
		System.out.println("startPage:"+startPage);
		
		Hotel h = hs.getHotel(hotelno);
		List<Hotelboard> list = hbs.listHotelboard(hotelno, start, end);
		for(Hotelboard b : list) {
			Member m= ms.getMember(b.getId());
			b.setId(m.getName()); 
			b.setRestep(hbs.hotel_board_count2(b.getRegroup()));
		}
		
		model.addAttribute("listBoard", list); 
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalRecord", totalRecord);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("startNum", startNum);
		model.addAttribute("pageGROUP", pageGROUP);
		model.addAttribute("hotelno", hotelno);
		System.out.println("pageNUM:"+pageNUM);

		model.addAttribute("h", h); 

		return "/hotel_board/list";
	}

	@GetMapping("/hotel_board/write/{hotelno}")
	public String insertForm(Model model,@PathVariable("hotelno") int hotelno) {	
		System.out.println("insert폼작동");
		model.addAttribute("hotelno", hotelno); 
		return "/hotel_board/write";
	}
	
	@PostMapping("/hotel_board/write/{hotelno}")
	public String insertBoard(Hotelboard b, MultipartHttpServletRequest req,HttpSession session, @PathVariable(value = "hotelno") int hotelno) {
		String view = "redirect:/hotel_board/list/"+b.getHotelno()+"/1";
		System.out.println(view);
		String path = req.getServletContext().getRealPath("/images/hotelboard");
		System.out.println("path:" + path);
		
	    List<MultipartFile> fileList = req.getFiles("uploadFile");
	    String filename = "";
	    for (MultipartFile mf : fileList) {
	        String fname = mf.getOriginalFilename();
	        if (!fname.isEmpty()) {
	            try {
	                mf.transferTo(new File(path + "/" + fname));
	                filename+=fname+"/";
	                b.setFilename(filename);
	            } catch (Exception e) {
	                System.out.println("예외발생: " + e.getMessage());
	            }
	        }
	    }
	    Member user = (Member) session.getAttribute("user");
	    String id= user.getId();
	    b.setId(id);
		int count = hbs.hotel_board_count();
		if (b.getHotelboardno() == 0){
			if(count!=0){
				int hotelboardno = hotelboarddao.getNextHotelboardno();
				b.setRegroup(hotelboardno);
			}else{
				b.setRegroup(1);
			}
		}else {
			hbs.hotelboard_reUP(b.getRestep(), b.getRegroup());
			b.setRestep(b.getRestep() + 1);
			b.setRelevel(b.getRelevel() + 1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        b.setRegdate(currentDateAndTime);
		hbs.insert(b);
		return view;
	}

	
	@GetMapping(value="/hotel_board/content/{hotelno}/{hotelboardno}/{pageNUM}")
	public String BoardContent(Model model, @PathVariable("pageNUM") int pageNUM, @PathVariable("hotelno") int hotelno,
			@PathVariable("hotelboardno") int hotelboardno) {
		hbs.read_count(hotelboardno);
		Hotelboard b = hbs.getHotelboard(hotelboardno);
		Member m = ms.getMember(b.getId());
		b.setId(m.getName()); 
		
		System.out.println("pageNUM:"+pageNUM);
		totalRecord = hbs.hotel_board_count2(b.getRegroup());
		totalPage =(int) Math.ceil((double)totalRecord/pageSIZE);
		int start = (pageNUM-1)*pageSIZE+1;
		int end = start+pageSIZE-1;

		int startNum = totalRecord-((pageNUM-1)*pageSIZE);
		int pageCount = totalRecord/pageSIZE + (totalRecord%pageSIZE == 0 ? 0 : 1);
		pageGROUP = 5;
		int startPage = (pageNUM-1)/pageGROUP * pageGROUP + 1;
		int endPage = startPage + pageGROUP - 1;
		if (endPage > pageCount) endPage = pageCount;

		System.out.println("start:"+start);
		System.out.println("end:"+end);
		System.out.println("pageGROUP:"+pageGROUP);
		System.out.println("startPage:"+startPage);
		
		List<Hotelboard> list = hbs.listHotelboard2(b.getRegroup(),start, end);
		for(Hotelboard hb :list) {
			Member m2 = ms.getMember(hb.getId());
			hb.setId(m2.getName()); 
		}
		model.addAttribute("getBoard", b); 
		model.addAttribute("listBoard", list); 
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalRecord", totalRecord);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("startNum", startNum);
		model.addAttribute("pageGROUP", pageGROUP);
		System.out.println("pageNUM:"+pageNUM);

		//filename split사용해서 끊어서 보내기
		if(b.getFilename() !=null) {
			String hotelfile = b.getFilename();
			String [] filearr = hotelfile.split("/");
			for (String item : filearr) {
			    System.out.println(item);
			}
		model.addAttribute("filearr", filearr);
		}
		model.addAttribute("hotelno", hotelno); 
		model.addAttribute("hotelboardno", hotelboardno); 
		String view = "/hotel_board/content";
		return view;
	}
	
	@PostMapping(value="/hotel_board/reinsert")
	public String insertBoard2(Model model, Hotelboard b,HttpSession session, @RequestParam int hotelno, @RequestParam int hotelboardno,
			@RequestParam int restep, @RequestParam int regroup, @RequestParam int relevel) {
		String view = "redirect:/hotel_board/content/"+hotelno+"/"+hotelboardno+"/1";
		Member user = (Member) session.getAttribute("user");
		b.setId(user.getId());
		int count = hbs.hotel_board_count();
		if (b.getHotelboardno() == 0){
			if(count!=0){
				int no = hotelboarddao.getNextHotelboardno();
				b.setRegroup(no);
			}else{
				b.setRegroup(1);
			}
		}else {
			hbs.hotelboard_reUP(b.getRestep(), b.getRegroup());
			b.setRestep(b.getRestep() + 1);
			b.setRelevel(b.getRelevel() + 1);
		}

		b.setTitle("1");
		b.setFilename("1");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        b.setRegdate(currentDateAndTime);
        
		hbs.insert(b);
		System.out.println(view);
		return view;
	}
	
	//댓글삭제
	@GetMapping("/hotel_board/delete/{hotelboardno}")
	public String delete(@PathVariable("hotelboardno") int hotelboardno, @RequestParam int hotelno, @RequestParam int no) {
		String view = "redirect:/hotel_board/content/"+hotelno+"/"+no+"/1";
		hbs.delete(hotelboardno);
		return view;
	}
	
	//board게시글삭제
	@GetMapping("/hotel_board/deleteboard/{hotelboardno}")
	public String deleteboard(@PathVariable("hotelboardno") int hotelboardno, @RequestParam int hotelno, HttpServletRequest request) {
		String view = "redirect:/hotel_board/list/"+hotelno+"/1";
		String path = request.getServletContext().getRealPath("/images/hotelboard");
		String oldFname = hbs.getHotelboard(hotelboardno).getFilename();
		System.out.println(oldFname);
		hbs.delete(hotelboardno);
		try {
			File file = new File(path +"/"+oldFname);
			file.delete();
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return view;
	}
	
	//board게시글수정
	@GetMapping("/hotel_board/updateboard/{hotelboardno}")
	public String updateboard(Model model, @PathVariable("hotelboardno") int hotelboardno, @RequestParam int hotelno) {
		String view = "/hotel_board/updateboard";
		Hotelboard list = hbs.getHotelboard(hotelboardno);
		model.addAttribute("hotelboard", list);
		model.addAttribute("hotelboardno", hotelboardno);
		return view;
	}
	
	@PostMapping("/hotel_board/updateboard/{hotelboardno}")
	public String busstation_updateSubmit(Hotelboard b,@PathVariable("hotelboardno") int hotelboardno, @RequestParam int hotelno, HttpServletRequest request, Model model) {
		String path = request.getServletContext().getRealPath("/images/hotelboard");
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
				hbs.update(b);
				File file = new File(path + "/" + oldFname);
				file.delete();
			}catch (Exception e) {
				System.out.println("파일 업로드 예외발생:"+e.getMessage());   
			}
		}else {   //첨부파일이 없는 경우
			hbs.update(b);
		}
		String view = "redirect:/hotel_board/list/"+hotelno+"/1";
		return view;
	}

	//--------------------------관리자페이지--------------------------
	
	@GetMapping("/hotelAD/hotel/list")
	public void hotel_list(Model model,HttpSession session) {
		Member user = (Member) session.getAttribute("user");
		String id = user.getId();
		List<Hotel> list = hs.ADlistHotel(id);
		model.addAttribute("list", list);
	}
	
	@PostMapping("/hotelAD/hotel/list")
	public String hotel_list2(HttpSession session, @RequestParam String search, @RequestParam String searchString, Model model) {
		List<Hotel> list;
		Member user = (Member) session.getAttribute("user");
		String id = user.getId();
        if (search.equals("hname")) {
            list = hs.find_hotelbyname(searchString);
        } else {
        	list = hs.ADlistHotel(id);
        }
	    model.addAttribute("list", list);
	    return "/hotelAD/hotel/list"; 
	}
	
	@GetMapping("/hotelAD/hotel/insert")
	public void hotel_insert() {
	}

	@PostMapping("/hotelAD/hotel/insert")
	public String insertSubmit(Hotel h, MultipartHttpServletRequest req, HttpSession session) {
	    String view = "redirect:/hotelAD/hotel/list";
	    Member user = (Member) session.getAttribute("user");
	    String id=user.getId();
	    h.setId(id);
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

	@GetMapping("/hotelAD/hotel/delete/{hotelno}")
	public String hotel_delete(Model model, @PathVariable("hotelno") int hotelno) {
		hs.delete(hotelno);
		String url = "/hotelAD/hotel/list";
		String msg = "호텔 삭제 성공";
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "message";
	}

	@GetMapping("/hotelAD/hotel/update/{hotelno}")
	public String bus_update(Model model, @PathVariable("hotelno") int hotelno) {
		Hotel h = hs.getHotel(hotelno);
		model.addAttribute("hotel",h);
		
		return "/hotelAD/hotel/update";
	}

	@PostMapping("/hotelAD/hotel/update")
	public String hotel_updateOK(Model model, Hotel h, MultipartHttpServletRequest req, @RequestParam(value = "id") String id) {
		String view = "redirect:/hotelAD/hotel/list";
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

	@GetMapping("/hotelAD/room/insert/{hotelno}")
	public String roominsert(@PathVariable("hotelno") int hotelno, Model model){
		model.addAttribute("hotelno", hotelno);
		return "/hotelAD/room/insert";
	}

	@PostMapping("/hotelAD/room/insert/{hotelno}")
	public String roominsertok(Model model, Room r, MultipartHttpServletRequest req, @PathVariable(value = "hotelno") int hotelno){
		System.out.println(hotelno);
		String view = "redirect:/hotelAD/room/list/1";
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

	@GetMapping("/hotelAD/room/list/{hotelno}")
	public String roomlist(Model model, @PathVariable(value = "hotelno") int hotelno, HttpSession session){
		List<Room> list = rs.listRoom2(hotelno);
		String[] filenames = new String[list.size()];
		String firstfilename="";
		String roomhotelno = "";
		//룸 리스트 메인 사진 한장 가져오기
		int index = 0;
		for(Room room : list){
			String temp = room.getRoomno();
			String[] arrtemp = temp.split("-");
			System.out.println(arrtemp[0]);
			roomhotelno = arrtemp[0];
			
			String filename= room.getFilename();
			String[] arrfilename = filename.split("/");
			System.out.println(arrfilename[0]);
			firstfilename = arrfilename[0];
			filenames[index] = firstfilename;
			index++;
		}
		
			
		model.addAttribute("filenames", filenames);
		model.addAttribute("roomhotelno", roomhotelno);
		model.addAttribute("list", list); 
		model.addAttribute("hotelno", hotelno); 

		return "/hotelAD/room/list";
	}
	
	@GetMapping("/hotelAD/room/roomcontent/{roomno}")
	public String roomcontent(Model model, @PathVariable("roomno") String roomno){
		Room r = rs.getRoom(roomno);
		//룸 사진 여러장 가져오기
		String roomfile = r.getFilename();
		String regex="/";
		String [] filearr = roomfile.split(regex);

		model.addAttribute("filearr", filearr);
		model.addAttribute("r", r);

		return "/hotelAD/room/roomcontent";
	}
	
	@GetMapping("/hotelAD/room/delete/{roomno}")
	public String room_delete(Model model, @PathVariable("roomno") String roomno) {
		rs.delete(roomno);
		String url = "/hotelAD/hotel/list";
		String msg = "호텔 룸 삭제 성공";
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "message";
	}

	@GetMapping("/hotelAD/room/update/{roomno}")
	public String room_update(Model model, @PathVariable("roomno") String roomno) {
		String view = "/hotelAD/room/update";
		Room r = rs.getRoom(roomno);
		model.addAttribute("r", r);
		return view;
	}
	
	@PostMapping("/hotelAD/room/update")
	public String room_updateOK(MultipartHttpServletRequest req, HttpSession session, Room r) {
		String view = "redirect:/hotelAD/hotel/list";
		String path = req.getServletContext().getRealPath("/images/room"); // 이미지 저장 위치
		System.out.println("path:" + path);

		List<MultipartFile> fileList = req.getFiles("uploadFile"); // 업로드된 파일 목록을 가져옵니다.

		// 새로운 파일 이름을 저장할 변수입니다.
		String filename = "";
		filename = r.getFilename();
		r.setFilename(filename);

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
				} catch (Exception e) {
					System.out.println("예외발생: " + e.getMessage());
				}
			}
		}

		// 파일 이름을 업데이트된 파일 이름으로 설정합니다.
		r.setFilename(filename);

		rs.update(r);

		return view;
	}


	//------------------------resv-----------------------------
	@GetMapping("/hotelAD/hotel_resv/content/{hotelresvno}")
	public String resv_show(Model model, @PathVariable("hotelresvno") int hotelresvno) {

		HotelResv resv = hrs.getHotelResv(hotelresvno);
		int hotelno = resv.getHotelno();
		String id = resv.getId();
		String roomno = resv.getRoomno();

		Member m = ms.getMember(id);
		Hotel h = hs.getHotel(hotelno);
		Room r = rs.getRoom(roomno);
		//룸 사진 여러장 가져오기
		String roomfile = r.getFilename();
		String regex="/";
		String [] filearr = roomfile.split(regex);

		model.addAttribute("filearr", filearr);
		model.addAttribute("resv", resv);
		model.addAttribute("m", m);
		model.addAttribute("h", h);
		model.addAttribute("r", r);
		model.addAttribute("hotelno", hotelno);
		model.addAttribute("roomno", roomno);
		return "/hotel_resv/content";
	}

	@GetMapping("/hotelAD/hotel_resv/list/{hotelno}")
	public String resv_list(Model model, @PathVariable(value = "hotelno") int hotelno) {
		System.out.println("hotelno:"+hotelno);
		String view = "/hotelAD/hotel_resv/list";
		Hotel h=hs.getHotel(hotelno);
		model.addAttribute("h", h);
		model.addAttribute("hotelno", hotelno);
		
		List<HotelResv> resvlist = hrs.ADlistHotelresv(hotelno);
		List<Room> roomlist = rs.listRoom2(hotelno);
		int resvno = hotelresvdao.getNextHotelresvno();
		// 룸 목록과 호텔 번호를 모델에 추가하여 뷰로 전달합니다.
		model.addAttribute("resvlist", resvlist);
		model.addAttribute("resvno", resvno);
		model.addAttribute("roomlist", roomlist);
		return view;
	}
	
	//예약리스트 페이지
	@GetMapping(value="/hotelAD/hotel_resv/paylistall")
	public String hotel_resv_user_resvlistall(HttpSession session, Model model){
		String msg="";
		String url="";
		if((Member)session.getAttribute("user")==null){//로그인 유효성검사
			msg = "해당서비스는 로그인이 필요합니다.로그인페이지로 이동";
			url = "/member/login";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}else{
			List<Map<String, Object>> pay_list = ps.listPayhotelresv();
			model.addAttribute("pay_list",pay_list);
			return "/hotelAD/hotel_resv/paylistall";
		}
		
	}
	
	@PostMapping("/hotelAD/hotel_resv/paylistall")
	public String hotel_resv_user_resvlistall2(HttpSession session, @RequestParam String search, @RequestParam String searchString, Model model) {
		List<Map<String, Object>> pay_list;
        if (search.equals("no")) {
        	pay_list = ps.listPayhotelresvbyNo(Integer.parseInt(searchString));
        }else if(search.equals("id")) {
        	pay_list = ps.listPayhotelresvbyId(searchString);
		}else if(search.equals("apply_num")) {
			pay_list = ps.listPayhotelresvbyApplynum(searchString);
		}else {
			pay_list = ps.listPayhotelresv();
        }
	    model.addAttribute("pay_list", pay_list);
	    return "/hotelAD/hotel_resv/paylistall"; 
	}
	

}
