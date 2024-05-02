package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Bus;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Member;
import com.example.demo.entity.Room;
import com.example.demo.service.BusService;
import com.example.demo.service.HotelService;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.youiwe.webservice.BitSms;
import lombok.Setter;

@Controller
@Setter
public class MemberController {
	@Autowired
	private MemberService ms;

	@Autowired
	private HotelService hs;
	
	@Autowired
	private BusService bs;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private BitSms sms;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@GetMapping("/sendSms")
	@ResponseBody
	public String send() {
		String from="01025598279";//보내는 사람의 전화번호(너나우리에 등록된번호: 선생님전화번호)
		String to="01039364691";
		String msg="";
		sms.sendMsg(from, to, msg); //문자전송
		
		//4자리코드로보내고싶어 
		Random r = new Random();
		int a = r.nextInt(10); //0-9까지
		int b = r.nextInt(10); 
		int c = r.nextInt(10); 
		int d = r.nextInt(10); 
		String data = a+""+b+""+c+""+d;
		msg = "코드번호: "+data;

		return "ok!";
	}
	
	@GetMapping("/sendCodeEmail")
	@ResponseBody
	public String sendCodeEmail(String email) {
		System.out.println("email:"+email);
		
		//6자리코드로보내고싶어 
		Random r = new Random();
		int a = r.nextInt(10); //0-9까지
		int b = r.nextInt(10); 
		int c = r.nextInt(10); 
		int d = r.nextInt(10); 
		int e = r.nextInt(10); 
		int f = r.nextInt(10); 
		String data = a+""+b+""+c+""+d+""+e+""+f;
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("[FLEX] 인증번호확인");
		mailMessage.setFrom("jisoo950225@gmail.com");
		mailMessage.setTo(email);
		mailMessage.setText("[FLEX]인증번호는 "+data+" 입니다.\n 정확히 입력해주세요.");
		
		mailSender.send(mailMessage); //메일보내기
				
		return "ok!";
	}

	@PostMapping("/sendCodeEmail")
	public void sendCodeEmail(  ) {
	}
	
	@GetMapping("/member/login")
	public void loginForm() {		
	}
	
	@GetMapping("/member/join")
	public void joinForm() {		
	}
	
	@PostMapping("/member/join")
	public String joinSubmit(Member m, Model model) {
		String jumin1 = m.getJumin1(); //950225
		String jumin2 = m.getJumin2(); //2222222
		
		int thisYear, userYear, age;
		Date today = new Date();
		thisYear = today.getYear()+1900;
		
		//주민번호로 부터 앞의 두자리를 잘라온다.
		String yearFormJumin = jumin1.substring(0, 2);
		//주민번호로 부터 성별을 의미하는 글자를 갖고온다.
		char check = jumin2.charAt(0);
		//출생연도를 계산한다.
		if(check == '1' || check=='2') {
			userYear = Integer.parseInt(yearFormJumin) + 1900;
		}else {
			userYear = Integer.parseInt(yearFormJumin) + 2000;
		}
		//나이를 계산한다.
		age = thisYear - userYear;
		if(age<20) {
			m.setMinor(1);
		}
		m.setMinor(0);
		m.setPoint(20000);
		ms.insert(m);
		String url = "/member/login";
		String msg = "회원가입 성공";
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "message";
	}
	
	//아이디 비밀번호 찾기 페이지 이동
	@GetMapping("/member/search/{mode}")
	public String MemberSearch(Model model, @PathVariable("mode") String mode) {	
		String view = "/member/search";
		model.addAttribute("mode",mode);
		return view;
	}
	
	@GetMapping("/member/findIdByEmail")
	@ResponseBody
	public String findIdByEmail(String email) {
		Member m = ms.findByEmail(email);
		return m!=null ? m.getId() : null;
	}

	@GetMapping("/member/findIdByPhone")
	@ResponseBody
	public String findIdByPhone(String phone) {
		Member m = ms.findByPhone(phone);
		return m!=null ? m.getId() : null;
	}

	
	@PostMapping("/member/search") 
    public String memberSearchOk(@RequestParam("mode") String mode, @RequestParam("searchString") String searchString,
                                 @RequestParam("jumin1") String jumin1, @RequestParam("jumin2") String jumin2,
                                 Model model) {
        String view = "/member/searchResult";
		List<Member> list = null;
        if(mode.equals("id")) {
            list = ms.searchMemberId(searchString, jumin1, jumin2);
        } else if(mode.equals("passwd")) {
            list = ms.searchMemberPasswd(searchString, jumin1, jumin2);
        }
        model.addAttribute("searchList", list);
        model.addAttribute("mode", mode);
        return view; 
    }

	//아이디 비밀번호 찾기 페이지 이동
	@GetMapping("/member/searchType/{type}")
	public String MemberSearchResult(Model model, @PathVariable("type") String type) {	
		String view = "/member/searchType";
		model.addAttribute("type",type);
		return view;
	}
	@GetMapping("/member/checkId")
	@ResponseBody
	public String checkId(String id) {
		String result = "T";
		if (ms.findById(id).isPresent()) {
			result = "F";
		}
		return result;
	}

	@GetMapping("/member/checkEmail")
	@ResponseBody
	public String checkEmail(String email) {
		String msg = null;
		if (ms.findByEmail(email)!=null) {
			msg = "존재하는 이메일입니다.\n다른 이메일을 입력해주세요.";
		}
		return msg;
	}

	@GetMapping("/member/checkPhone")
	@ResponseBody
	public String checkPhone(String phone) {
		System.out.println("phone : "+phone);
		String result = "T";
		if (ms.findByPhone(phone)!=null) {
			result = "F";
		}
		return result;
	}

	@GetMapping("/member/isVaildEmail")
	@ResponseBody
	public String code(String email, HttpSession session) {
		String code = sendEmail(email);
		session.setAttribute("code", code);
		session.setMaxInactiveInterval(180);
		System.out.println("code : " + code);
		return code;
	}

	@PostMapping("/member/isVaildEmail")
	@ResponseBody
	public String isVaildEmail(String code, HttpSession session) {
		String trueCode = session.getAttribute("code").toString();
		if (trueCode.equals(code)) {
			return "T";
		} else {
			return "F";
		}
	}
	
	@GetMapping("/member/mypage") //마이페이지     
	public String MemberMypage(){
		return "/member/mypage";
	}
	
	private String sendEmail(String email) {
		System.out.println("email:"+email);
		
		//6자리코드로보내고싶어 
		Random r = new Random();
		int a = r.nextInt(10); //0-9까지
		int b = r.nextInt(10); 
		int c = r.nextInt(10); 
		int d = r.nextInt(10); 
		int e = r.nextInt(10); 
		int f = r.nextInt(10); 
		String data = a+""+b+""+c+""+d+""+e+""+f;
		String text = "<h2>[FLEX] 회원가입을 위한 인증코드입니다.</h2>" + "<hr>" + "<h3>인증코드 : " + data + "</h3>";
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("[FLEX] 인증번호확인");
		mailMessage.setFrom("jisoo950225@gmail.com");
		mailMessage.setTo(email);
		mailMessage.setText("[FLEX]인증번호는 "+data+" 입니다.\n 정확히 입력해주세요.");
		
		mailSender.send(mailMessage); //메일보내기

		return data;
	}

	@GetMapping("/member/changePwd")
	@ResponseBody
	public int changePwd(String pwd, String id, String email) {
		Member u = ms.getMember(id);
		int re = 0;
		if(u.getEmail().equals(email)) {
			String newPwd = passwordEncoder.encode(pwd);
			re = ms.changePasswd(id, newPwd);
			System.out.println("newPwd : " + newPwd);
			System.out.println("result : " + re);
		}else {
			re = -1;
		}
		return re;
	}

	private String makePwd() {
		StringBuilder pwd = new StringBuilder();
		Random r = new Random();
		pwd.append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9))
				.append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A'));
		return pwd.toString();
	}
	
	//superAD---------------------------------------------
	@GetMapping("/superAD/main")
	public String SuperAD(HttpServletRequest req) {
		return "/superAD/main";
	}
	
	@GetMapping("/superAD/member/list")
	public String memberList(Model model) {
	    List<Member> list = ms.memberList();
	    model.addAttribute("memberList", list);
	    return "/superAD/member/list"; 
	}
	@GetMapping("/superAD/member/listsearch")
	public String memberListsearch(Model model) {
		List<Member> list = ms.memberList();
	    model.addAttribute("memberList", list);
	    return "/superAD/member/listsearch"; 
	}
	@PostMapping("/superAD/member/listsearch")
	public String memberListsearch2(@RequestParam String search, @RequestParam String searchString, Model model) {
	    List<Member> list;
        if (search.equals("id")) {
            list = ms.findMemberId(searchString);
        } else if(search.equals("name")){
            list = ms.findMemberName(searchString);
        } else {
        	list = ms.memberList();
        }
	    model.addAttribute("memberList", list);
	    return "/superAD/member/listsearch"; 
	}
	
	@GetMapping("/superAD/member/update/{id}")
	public String MemberEdit(@PathVariable("id") String id, Model model){
		String view = "/superAD/member/update";
		Member m = ms.getMember(id);
		System.out.println("filename:"+m.getFilename());
		model.addAttribute("m", m);
		return view;
	}
	
	@PostMapping("/superAD/member/update")
	public String member_updateSubmit(Member m, HttpServletRequest request, HttpSession session, Model model) {
		String view = "redirect:/superAD/member/list";
		Member user = (Member) session.getAttribute("user");
		String role = user.getRole();
		String path = request.getServletContext().getRealPath("/images/member");
		System.out.println("path:"+path);
		String fname = null;
		String oldFname = null;
		oldFname= m.getFilename();
		System.out.println("oldFname이 설정됨"+oldFname);
		MultipartFile uploadFile = m.getUploadFile();
		fname = uploadFile.getOriginalFilename();
		System.out.println("fname: "+fname);
		
		if(fname != null && !fname.equals("")) {   //첨부파일이 있는 경우
			try {
				FileOutputStream fos = new FileOutputStream(path+"/"+fname);            
				FileCopyUtils.copy(uploadFile.getBytes(), fos);
				fos.close();
				m.setFilename(fname);
				System.out.println("fname이 설정됨"+fname);
				ms.update(m);
				File file = new File(path + "/" + oldFname);
				file.delete();
			}catch (Exception e) {
				System.out.println("파일 업로드 예외발생:"+e.getMessage());   
			}
		}else {   //첨부파일이 없는 경우
			ms.update(m);
		}
		if(role.equals("user")) {
			String msg = "수정이 완료되었습니다.";
			String url = "/";
			model.addAttribute("msg",msg);
			model.addAttribute("url",url);
			return "message";
		}
		return view;
	}

	@GetMapping("/superAD/member/delete/{id}")
	public String delete(@PathVariable("id") String id) {
		String view = "redirect:/superAD/member/list";
		ms.deleteMember(id);
		return view;
	}
	
	//버스관리자리스트
	@GetMapping("/superAD/BAD_list")
	public String Member_BAD(Model model) {
		String view = "/superAD/BAD_list";
		List<Member> alllist = ms.memberList();
		for(Member member : alllist){
			String role = member.getRole();
			if(role.equals("admin")) {
				List<Member> list = ms.getPosition(role);
				System.out.println("list.size:"+list.size());
				model.addAttribute("list", list);
			}
		}
		return view;
	}

	@GetMapping("/superAD/HAD_list")
	public String Member_HAD(Model model) {
		String view = "/superAD/HAD_list";
		List<Member> alllist = ms.memberList();
		for(Member member : alllist){
			String role = member.getRole();
			if(role.equals("hadmin")) {
				List<Member> list = ms.getPosition(role);
				System.out.println("list.size:"+list.size());
				model.addAttribute("list", list);
			}
		}
		return view;
	}
	
	@GetMapping("/superAD/AD_list")
	public String Member_AD(Model model) {
		String view = "/superAD/AD_list";
		List<Member> alllist = ms.memberList();
		for(Member member : alllist){
			String role = member.getRole();
			if(role.equals("hadmin") || role.equals("admin")) {
			    List<Member> list = new ArrayList<>();
			    list.addAll(ms.getPosition("hadmin"));
			    list.addAll(ms.getPosition("admin"));
			    System.out.println("list.size:" + list.size());
			    model.addAttribute("list", list);
			}
		}
		return view;
	}
	
	@GetMapping("/user_list")
	public String Member_user(Model model) {
		String view = "/user_list";
		List<Member> alllist = ms.memberList();
		for(Member member : alllist){
			String role = member.getRole();
			if(role.equals("user")) {
				List<Member> list = ms.getPosition(role);
				System.out.println("list.size:"+list.size());
				model.addAttribute("list", list);
			}
		}
		return view;
	}

	//관리중인 호텔
	@GetMapping("/superAD/HADhotel_list/{id}")
	public String hotel_list(@PathVariable("id") String id, Model model) {
		String view = "/superAD/HADhotel_list";
		List<Hotel> list = hs.ADlistHotel(id);
		model.addAttribute("list", list);
		model.addAttribute("id", id);
		return view;
	}
	
	//관리중인 버스
	@GetMapping("/superAD/BADbus_list/{id}")
	public String bus_list(@PathVariable("id") String id, Model model) {
		String view = "/superAD/BADbus_list";
		List<Bus> list = bs.ADlistBus(id);
		model.addAttribute("list", list);
		model.addAttribute("id", id);
		return view;
	}
}

