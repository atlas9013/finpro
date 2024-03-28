package com.example.demo;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import kr.co.youiwe.webservice.BitSms;

//XML대신에 객체를 재공해주는 환경설정 파일 어노테이션
@Configuration
public class SpringConfig {
   
	@Bean
	public BitSms sms() {
	   //객체를 생성해주는id를 만들자
	   return new BitSms();
   }
	
   //xml의 bean이라는 태그를 대신하는 메소드 /메소드의 역할이 xml의 id
   @Bean //객체 제공자임을 알려주는 annotation
   public JavaMailSenderImpl javaMailSender() {//메일보내기객체생성
	  JavaMailSenderImpl jms = new JavaMailSenderImpl();
	  
      jms.setHost("smtp.gmail.com"); //gmail로보내기사용할거야
      jms.setPort(587);
      jms.setUsername("jisoo950225@gmail.com");//정보입력
      jms.setPassword("");//발행받은암호
      jms.setDefaultEncoding("UTF-8");
      
      Properties prop = new Properties();
      prop.put("mail.smtp.starttls.enable", true);
      prop.put("mail.smtp.auth", true);
      prop.put("mail.smtp.ssl.checkserveridentity", true);
      prop.put("mail.smtp.ssl.trust", "*");
      prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
      
      jms.setJavaMailProperties(prop);//설정해주면 끝
      return jms;
   }
}