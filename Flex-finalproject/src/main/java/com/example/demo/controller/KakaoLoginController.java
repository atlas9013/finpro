package com.example.demo.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Member;
import com.example.demo.service.KakaoLoginService;
import com.example.demo.service.MemberService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {
	
	@Autowired
	private MemberService ms;
	
	@Autowired
	private KakaoLoginService ks;
	
	@GetMapping("/kakao/callback")
	public String KakaoLogin(@RequestParam(name="code") String code, Model model, HttpSession session) {
		System.out.println("코드:"+code);
		String access = ks.getKakaoAccessToken(code);
		System.out.println(" 리턴:"+access);
		String info = ks.createKakaoUser(access);
		System.out.println("정보:"+info);
		
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(info);
        
        String id = "kakao@"+element.getAsJsonObject().get("id").getAsString();
        JsonElement kakaoAccount = element.getAsJsonObject().get("kakao_account");
        //카카오계정 안에있는 profile가져옴
        JsonElement profile = kakaoAccount.getAsJsonObject().get("profile");
        String name = kakaoAccount.getAsJsonObject().get("name").getAsString();
        String phone = kakaoAccount.getAsJsonObject().get("phone_number").getAsString();
        String birthyear = kakaoAccount.getAsJsonObject().get("birthyear").getAsString();
        String birthday = kakaoAccount.getAsJsonObject().get("birthyear").getAsString();
		
        System.out.println("생년:"+birthyear); //1995
        System.out.println("월일:"+birthday); //0225
        
        boolean hasEmail = false;
        String email = "";
        Date birth = null;
        String jumin = birthyear.substring(birthyear.length() - 2)+birthday;
		
        //null이아니고 json값이 null이 아닐때 이메일 정보 확인
        if(kakaoAccount !=null && !kakaoAccount.isJsonNull()) {
        	JsonElement hasEmailElement = kakaoAccount.getAsJsonObject().get("has_email");
        	if(hasEmailElement !=null && !hasEmailElement.isJsonNull()) {
        		hasEmail = hasEmailElement.getAsBoolean();
        	}
        	
        	if(hasEmail) {
        		JsonElement emailElement = kakaoAccount.getAsJsonObject().get("email");
        		if(emailElement !=null && !emailElement.isJsonNull()) {
            		email = emailElement.getAsString();
            	}
        	}
        }
        //기호제거
        phone = phone.replaceAll("[\\+\\-]", "");
		if(phone.startsWith("82")) {
			//82제거
			phone = phone.substring(2);
		}
		
		phone = phone.replaceAll("\\s", "");
		if(!phone.startsWith("0")) {
			phone = "0"+phone;
		}

		System.out.println("이름:"+name);
		System.out.println("email:"+email);
		System.out.println("전화번호:"+phone);
		System.out.println("jumin1:"+jumin);
		//아이디로 유저판단해야-> 사용자가없다면 회원가입페이지로 이동
		Member m = ms.findById(id).get();
		StringBuilder pwd = new StringBuilder();
		Random r = new Random();
		pwd.append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9))
				.append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A'));
		if(m == null) {
			m = new Member();
			m.setId(id);
			m.setPasswd(pwd.toString());
			m.setName(name);
			m.setJumin1(jumin);
			m.setJumin2("1111111");
			m.setPhone(phone);
			m.setEmail(email);
			
			model.addAttribute("m", m);
			return "redirect:/member/join";
		}
		session.setAttribute("id", m.getId());
		return "redirect:/";
	}
}

