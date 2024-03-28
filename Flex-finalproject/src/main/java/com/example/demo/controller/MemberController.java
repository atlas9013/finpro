package com.example.demo.controller;

import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;
import kr.co.youiwe.webservice.BitSms;
import lombok.Setter;

@Controller
@Setter
public class MemberController {
	@Autowired
	private MemberService ms;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private BitSms sms;
	
	@GetMapping("/sendSms")
	@ResponseBody
	public String send() {
		String from="";//보내는 사람의 전화번호(너나우리에 등록된번호: 선생님전화번호)
		String to="";
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
	public String joinSubmit(Member m) {
		String view = "/member/login";
		ms.insert(m);
		return view;
	}
	
	//아이디 비밀번호 찾기 페이지 이동
	@GetMapping("/member/search/{mode}")
	public String MemberSearch(Model model, @PathVariable("mode") String mode) {	
		String view = "/member/search";
		model.addAttribute("mode",mode);
		return view;
	}
	
	@PostMapping("/member/search") // 아이디 비밀번호 찾기 완료
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
		if (ms.findByEmail(email).isPresent()) {
			msg = "존재하는 이메일입니다.\n다른 이메일을 입력해주세요.";
		}
		return msg;
	}

	@GetMapping("/member/checkPhone")
	@ResponseBody
	public String checkPhone(String phone) {
		System.out.println("phone : "+phone);
		String result = "T";
		if (ms.findByPhone(phone).isPresent()) {
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

//	@PostMapping("/member/findIdByEmail")
//	@ResponseBody
//	public String findIdByEmail(String email) {
//		Optional<Member> u = ms.findByEmail(email);
//		return u.isPresent() ? u.get().getId() : null;
//	}
//
//	@PostMapping("/member/findIdByPhone")
//	@ResponseBody
//	public String findIdByPhone(String phone) {
//		Optional<Member> u = ms.findByPhone(phone);
//		return u.isPresent() ? u.get().getId() : null;
//	}
//
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

//	@PostMapping("/member/changePwd")
//	@ResponseBody
//	public int changePwd(String pwd, String email) {
//		Member u = ms.findByEmail(email).get();
//		String newPwd = encoder.encode(pwd);
//		int re = ms.changePwd(u.getId(), newPwd);
//		System.out.println("newPwd : " + newPwd);
//		System.out.println("result : " + re);
//		return re;
//	}
//
//	private String makePwd() {
//		StringBuilder pwd = new StringBuilder();
//		SecureRandom r = new SecureRandom();
//		pwd.append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9))
//				.append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A'));
//		return pwd.toString();
//	}

}

