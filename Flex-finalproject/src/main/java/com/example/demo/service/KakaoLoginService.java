package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Service
public class KakaoLoginService {
	
	public String getKakaoAccessToken (String code) {
		String accessToken = "";
		String refreshToken = "";
		String requestURL = "https://kauth.kakao.com/oauth/token";
		
		//액세스토큰요청
		try {
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//URL 객체를 사용하여 원격서버에 HTTP 연결
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// setDoOutput()은 OutputStream으로 POST 데이터를 넘겨 주겠다는 옵션
			// POST 요청을 수행하려면 setDoOutput()을 true로 설정
			conn.setDoOutput(true);
			
			// POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String sb = "grant_type=authorization_code" +
					"&client_id=6d9e6a1baa7d5ea9b7935f98ce6638a0" + 
					"&redirect_uri=http://localhost:8088/kakao/callback" + // REDIRECT_URI
					"&code=" + code;
			bufferedWriter.write(sb);
			bufferedWriter.flush();
			
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			//결과 코드가 200이라면 성공
			
			// 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			StringBuilder result = new StringBuilder();
			
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			System.out.println("response body : " + result);
			
			JsonElement element = JsonParser.parseString(result.toString());
			
			accessToken = element.getAsJsonObject().get("access_token").getAsString();
			refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
			
			System.out.println("accessToken : " + accessToken);
			System.out.println("refreshToken : " + refreshToken);
			
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return accessToken;
	}
	
	public String createKakaoUser(String accessToken) {
	    String postURL = "https://kapi.kakao.com/v2/user/me";
	    String line = "";
	    String result = "";

	    try {
	        URL url = new URL(postURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

	        //결과코드가 200이면 성공
	        int responseCode = conn.getResponseCode();
	        System.out.println("responseCode : " + responseCode);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	        while ((line = br.readLine()) != null) {
	            result += line;
	        }
	        System.out.println("response body : " + result);

	        br.close();

	    } catch (IOException exception) {
	        exception.printStackTrace();
	    }
	    
	    return result;
	}
	
	public String makePwd() {
	    StringBuilder pwd = new StringBuilder();
	    SecureRandom r = new SecureRandom();
	    pwd.append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9))
	            .append((char) (r.nextInt(26) + 'A')).append(r.nextInt(9)).append((char) (r.nextInt(26) + 'A'));
	    return pwd.toString();
	}

}
