package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.dao.MemberDAO;
import com.example.demo.entity.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private MemberDAO dao;
	
	@Autowired
	CustomSuccessHandler handler;
	
	@Autowired
	KakaoOAuth2UserService kakaoOAuth2UserService;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.authorizeHttpRequests()
		.requestMatchers("/", "/member/login", "/member/join", "/member/mypage", "/bus/list/**","/busstation/list/**","/busresv/list/**","/busroad/list/**", "/hotel/**", "/hotel/hotelcontent/**","/room/list/**", "/room/roomcontent/**", "/user_busstation/**", "/user_busresv/**", "/hotel_board/**", "/hotel_resv/**", "/superAD/member/**" ).permitAll()
		.requestMatchers("/goods/insert", "/bus/**","/busstation/**", "/busresv/**",
				"/busroad/**", "/member/**", "/user_busstation/**", "/user_busresv/**", "/hotel/**","/hotel_resv/**", "/hotel_board/**", "/hotelAD/**", "/superAD/**" ).hasAnyRole("admin", "alladmin", "hadmin")
		//.requestMatchers().hasRole("user")
		.anyRequest().authenticated();
		
		http.formLogin().loginPage("/member/login").permitAll()
		.successHandler(successHandler()).failureHandler(failureHandler())
		.defaultSuccessUrl("/");
		
		http.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.invalidateHttpSession(true)
		.logoutSuccessUrl("/member/login");
		
		http.httpBasic();
		
		return http.build();
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	private class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
			String id = authentication.getName();
			Member user = dao.findById(id).get();
			user.setPasswd(null);
			System.out.println(id + " 로그인 진행중 : " + user);
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			
//			if(user.getRole()=="user") {
//				response.sendRedirect("/");				
//			} else {
//				response.sendRedirect("/adminDashBoard");
//			}
			
		}
	}

	private class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			String id = request.getParameter("username");
			String error = exception.getMessage();
			System.out.println("error : " + error);
			request.setAttribute("error", error);
			response.sendRedirect("/member/login");
		}
	}

	
}

