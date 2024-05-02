package com.example.demo.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Hotel;
import com.example.demo.service.HotelService;

import lombok.Setter;

@RestController
@Setter
public class CrawlingController {
	
	@Autowired
	private HotelService hs;
	
	@GetMapping("/newhotel")
	public String newhotel() {
		String filename = "";
		String filename1 = "";
        String base = "https://www.google.co.kr";
        String url =
                "https://www.google.co.kr/travel/search?q=%EC%84%9C%EC%9A%B8&ts=CAESCgoCCAMKAggDEAAaHBIaEhQKBwjoDxAEGAwSBwjoDxAEGA0YATICCAEqDAoIEgEFOgNLUlcaAA&ved=0CAAQ5JsGahgKEwjgn7LsubaFAxUAAAAAHQAAAAAQrBA&ictx=3&ap=MAA&qs=CAAgACgA";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements list = doc.select(".PVOOXe");
            for (Element e : list) {
                String name = e.attr("aria-label");
                String name2 = name.replaceAll("\\s", "").replaceAll("호텔", "").replaceAll("hotel", "").replaceAll("&", "").replaceAll(",", ""); // 공백 제거
                System.out.println(name);
                String link = base + e.attr("href");
                System.out.println(link);
                Document doc2 = Jsoup.connect(link).get();

                Elements span = doc2.select(".OGAsq").get(0).getElementsByTag("span");
				int star = Integer.parseInt(span.get(1).text().replaceAll("[^0-9]", ""));
				System.out.println(star);
				String address = span.get(2).text();
				Elements span1=null;
				if (address.startsWith("환경 보호 인증") || address.equals("•")) {
					span1 = doc2.select(".K4nuhf").get(0).getElementsByTag("span");
					address = span1.get(0).text();
				}
				System.out.println(address);
				String phone = span.get(4).text();
				if(phone.startsWith("환경 보호 인증")) {
					phone = span1.get(2).text();
				}
				System.out.println(phone);
				
				Elements divElements = doc2.select("div.y3yqve.QB2Jof");
				String info = divElements.isEmpty() ? "" : divElements.get(0).text();
				System.out.println(info);
				
//				Hotel hotel = new Hotel();
//				hotel.setName(name);
//				hotel.setAddress(address);
//				hotel.setPhone(phone);
//				hotel.setId("hong2");
//				hotel.setInfo(info);
//				hotel.setStar(star);
				
//				Elements elements = doc2.select(".olLA5e.FMaDxf");
//				int i = 1; // 시작 인덱스
//				filename1 = "";
//				for (Element ex : elements) {
//				    Elements img = ex.getElementsByTag("img");
//				    String src = img.attr("data-src");
//				    System.out.println(src);
//				    filename = imageDownload(src, name2, i); // 순서(index)를 함께 전달
//				    System.out.println(filename);
//				    i++; // 인덱스 증가
//				    filename1 += filename+"/";
////				    hotel.setFilename(filename1);
//				}
				System.out.println("----------------------");
				
				
//				hotel.setFilename(filename1);
//				hs.insert(hotel);
            }

        } catch (Exception e) {
        	System.out.println("예외발생:"+e.getMessage());
        }
        
        return "ok";
    }
	
	public String imageDownload(String arr, String fname, int i) {
		String filename = "";
		try {
			URL url = new URL(arr); //전체합쳐진 arr을 받으려고
			String fname1 = fname+i;
			InputStream is = url.openStream();
			filename = fname1 + ".jpg";
			FileOutputStream fos = new FileOutputStream("c:/data/"+filename);
			FileCopyUtils.copy(is.readAllBytes(), fos);
			is.close();
			fos.close();
			System.out.println(fname+"을 다운로드하였습니다.");
		}
		catch (Exception e) {
			System.out.println("예외발생: "+e.getMessage());
		}
		return filename;
	}
	
}