package com.example.demo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	MemberService as;
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		 System.out.println("kakao success handler");
         try {
            System.out.println("id : "+authentication.getDetails());
            String id = authentication.getName();
            HttpSession session = request.getSession();
            Member a = as.findById(id).get();
            if (a.getId() == null) {
            	a.setName("");
            	session.setAttribute("a", a);
            	response.sendRedirect("/member/updateKakaoAccount");
            }else {
            	a.setPasswd(null);
            	session.setAttribute("a", a);
            	response.sendRedirect("/");
            	
            }
         } catch (Exception e) {
            System.out.println("error : "+e.getMessage());
         
      }
   }
    }


