package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.HotelBoardDAO;
import com.example.demo.entity.Hotelboard;

import lombok.Setter;

@Service
@Setter
public class HotelBoardService {
	
	@Autowired
	private HotelBoardDAO dao;

	public List<Hotelboard> listHotelboard(int hotelno,int start, int end){
		return dao.listHotelboard(hotelno,start,end);
	}
	public List<Hotelboard> listHotelboard2(int regroup, int start, int end){
		return dao.listHotelboard2(regroup,start,end);
	}
	public int hotelboardcountbyHotelno(int hotelno){
		return dao.hotelboardcountbyHotelno(hotelno);
	}
	
	public String hotelboardregdatebyHotelno(int hotelno){
		return dao.hotelboardregdatebyHotelno(hotelno);
	}
	public void insert(Hotelboard h) {
		h.setHotelboardno(dao.getNextHotelboardno());
		dao.save(h);
	}
	public int delete(int hotelboardno){
		return dao.delete(hotelboardno);
	}
	public Hotelboard getHotelboard(int hotelboardno) {
		return dao.findById(hotelboardno).get();
	}
	public void update(Hotelboard h){
		dao.save(h);
	}
	public int count() {
		return (int)dao.count();
	}
	public int hotel_board_count() {
		return dao.hotelboardcount();
	}
	public int hotel_board_count2(int regroup) {
		return dao.hotelboardcount2(regroup);
	}
	public int hotelboard_reUP(int restep, int regroup) {
		return dao.hotelboard_reUP(restep, regroup);
	}
	public int read_count(int hotelboardno){
		return dao.read_count(hotelboardno);
	}
}
