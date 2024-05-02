package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.HotelDAO;
import com.example.demo.entity.Hotel;

import lombok.Setter;

@Service
@Setter
public class HotelService {
	@Autowired
	private HotelDAO dao;

	public List<Hotel> listHotel(){
		return dao.findAllByOrderByHotelnoDesc();
	}
	public List<Hotel> ADlistHotel(String id){
		return dao.ADlistHotel(id);
	}
	public List<Hotel> hotel_list_count(int start,int end){
		return dao.hotel_list_count(start,end);
	}
	public List<Hotel> listHoteladdress(String address){
		return dao.listHoteladdress(address);
	}
	public void insert(Hotel h) {
		h.setHotelno(dao.getNextHotelno());
		dao.save(h);
	}
	public int delete(int hotelno){
		return dao.delete(hotelno);
	}
	public Hotel getHotel(int hotelno) {
		return dao.findById(hotelno).get();
	}
	public void update(Hotel h){
		dao.save(h);
	}
	public int count() {
		return (int)dao.count();
	}
	public List<Hotel> find_hotel(String keyword, int start,int end){
		return dao.find_hotel(keyword, start,end);
	}
	public int find_hotel_count(String keyword) {
		return dao.find_hotel_count(keyword);
	}
	public List<Hotel> find_hotelbyname(String keyword){
		return dao.find_hotelbyname(keyword);
	}
}
