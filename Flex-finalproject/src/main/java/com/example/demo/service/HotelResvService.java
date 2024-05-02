package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BusDAO;
import com.example.demo.dao.HotelResvDAO;
import com.example.demo.entity.Bus;
import com.example.demo.entity.BusResv;
import com.example.demo.entity.HotelResv;

import lombok.Setter;

@Service
@Setter
public class HotelResvService {
	
	@Autowired
	private HotelResvDAO dao;

	public List<HotelResv> listHotelResv(){
		return dao.findAllByOrderByHotelresvno();
	}
	
	public List<HotelResv> ADlistHotelresv(int hotelno){
		return dao.ADlistHotelresv(hotelno);
	}
	
	public List<HotelResv> hotelresv(String id){
		return dao.hotelresv(id);
	}
	
	public void insert(HotelResv h) {
		h.setHotelresvno(dao.getNextHotelresvno());
		dao.save(h);
	}
	public int delete(int hotelresvno){
		return dao.delete(hotelresvno);
	}
	public HotelResv getHotelResv(int hotelresvno) {
		return dao.findById(hotelresvno).get();
	}
	public void update(HotelResv h){
		dao.save(h);
	}
	public int count() {
		return (int)dao.count();
	}
	
}
