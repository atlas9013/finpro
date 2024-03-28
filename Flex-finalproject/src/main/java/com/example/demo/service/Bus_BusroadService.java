package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BusDAO;
import com.example.demo.dao.BusRoadDAO;
import com.example.demo.dao.Bus_BusRoadDAO;
import com.example.demo.entity.Bus;
import com.example.demo.entity.BusBusRoad;
import com.example.demo.entity.BusRoad;

import lombok.Setter;

@Service
@Setter
public class Bus_BusroadService {
	@Autowired
	private Bus_BusRoadDAO dao;

//	public List<Bus_BusRoad> listResv(String arrival, String departure, String grade){
//		return dao.listResv(arrival,departure,grade);
//	}
//	public int listResv_count(String arrival, String departure, String grade, int start, int end){
//		return dao.listResv_count(arrival,departure,grade,start,end);
//	}
//	public int listResv_reverse_count(String departure, String arrival, String grade, int start, int end) {
//		return dao.listResv_reverse_count(departure, arrival, grade, start, end);
//	}
//	
//	public List<Bus_BusRoad> listResv_reverse(String departure, String arrival, String grade){
//		return dao.listResv_reverse(departure, arrival, grade);
//	}
//	
//	public List<Bus_BusRoad> listResv_all(String arrival, String departure){
//		return dao.listResv_all(arrival, departure);
//	}
//	public int listResv_all_count(String arrival, String departure, int start, int end){
//		return dao.listResv_all_count(arrival, departure, start, end);
//	}
//	public List<Bus_BusRoad> listResv_all_reverse(String departure, String arrival){
//		return dao.listResv_all_reverse(departure, arrival);
//	}
//	public List<Bus_BusRoad> listResv_all_reverse_count(String departure, String arrival, int start, int end){
//		return dao.listResv_all_reverse_count(departure, arrival, start, end);
//	}
//	public List<Bus_BusRoad> resv_user_seat_select(int roadno){
//		return dao.resv_user_seat_select(roadno);
//	}
//	public int resverse_count(String departure, String arrival, String grade) { 
//		return dao.resverse_count(departure, arrival, grade);
//	}
//	public int resverse_all_count(String departure, String arrival) { 
//		return dao.resverse_all_count(departure, arrival);
//	}
	public List<BusBusRoad> bus_no_list_null(){
		return dao.bus_no_list_null();
	}
	public int bus_no_list_null_count() { 
		return dao.bus_no_list_null_count();
	}
	public BusBusRoad bus_no_null_rownum() {
		return dao.bus_no_null_rownum();
	}
	public void insert(BusBusRoad b) {
		dao.save(b);
	}
	public BusBusRoad getBusroad(int busno) {
		return dao.findById(busno).get();
	}
	public void update(BusBusRoad b){
		dao.save(b);
	}
	public int count() {
		return (int)dao.count();
	}
}
