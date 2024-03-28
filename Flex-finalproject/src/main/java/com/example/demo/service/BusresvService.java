package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BusResvDAO;
import com.example.demo.entity.BusBusRoad;
import com.example.demo.entity.BusResv;
import com.example.demo.entity.BusResv_BusRoad;

import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class BusresvService {
	@Autowired
	private BusResvDAO dao;

	public List<BusResv> listBusresv(){
		return dao.findAllByOrderByResvno();
	}
	public void insert(BusResv b) {
		b.setResvno(dao.getNextResvno());
		dao.save(b);
	}
	public int delete(int resvno){
		return dao.delete(resvno);
	}
	public BusResv getBusresv(int resvno) {
		return dao.findById(resvno).get();
	}
	public void update(BusResv b){
		dao.save(b);
	}
	public List<BusResv> busresv_list_count(int start, int end){
		return dao.busresv_list_count(start, end);
	}

	public List<BusResv_BusRoad> resvlist(String id){
		return dao.resvlist(id);
	}
	
	public List<BusResv> list_seat_resv_user(String resvdate, int roadno){
		return dao.list_seat_resv_user(resvdate, roadno);
	}

	public int count() {
		return (int)dao.count();
	}
	
	public List<BusBusRoad> list_seat_resv_user(int arrival, int departure, String grade){
		return dao.list_seat_resv_user(arrival, departure, grade);
	}
	
	public List<BusBusRoad> listDispatch_resv_count(int arrival, int departure, String grade, int start, int end){
		return dao.listDispatch_resv_count(arrival, departure, grade, start, end);
	}
	
	public List<BusBusRoad> listDispatch_resv_reverse_count1(int departure, int arrival, String grade){
		return dao.listDispatch_resv_reverse_count1(departure, arrival, grade);
	}
	
	public List<BusBusRoad> listDispatch_resv_reverse_count(int departure, int arrival, String grade, int start, int end){
		return dao.listDispatch_resv_reverse_count(departure, arrival, grade, start, end);
	}
	
	public List<BusBusRoad> listDispatch_resv_reverse(int departure, int arrival, String grade){
		return dao.listDispatch_resv_reverse(departure, arrival, grade);
	}
	
	public List<BusBusRoad> listDispatch_resv_all(int arrival, int departure){
		return dao.listDispatch_resv_all(arrival, departure);
	}
	
	public List<BusBusRoad> listDispatch_resv_all_count1(int arrival, int departure, int start, int end){
		return dao.listDispatch_resv_all_count(arrival, departure, start, end);
	}
	
	public List<BusBusRoad> listDispatch_resv_all_count(int arrival, int departure, int start, int end){
		return dao.listDispatch_resv_all_count(arrival, departure, start, end);
	}
	
	public List<BusBusRoad> listDispatch_resv_all_reverse(int departure, int arrival){
		return dao.listDispatch_resv_all_reverse(departure, arrival);
	}
	
	public List<BusBusRoad> listDispatch_resv_all_reverse_count(int departure, int arrival, int start, int end){
		return dao.listDispatch_resv_all_reverse_count(departure, arrival, start, end);
	}
	
	public BusBusRoad resv_user_seat_select(int roadno){
		return dao.resv_user_seat_select(roadno);
	}
	
	public int bus_busroad_resv_count(int arrival, int departure, String grade) {
		return dao.bus_busroad_resv_count(arrival, departure, grade);
	}
	
	public int bus_busroad_resv_resverse_count(int departure, int arrival, String grade) {
		return dao.bus_busroad_resv_resverse_count(departure, arrival, grade);
	}
	
	public int bus_busroad_resv_resverse_all_count(int departure, int arrival) {
		return dao.bus_busroad_resv_resverse_all_count(departure, arrival);
	}
	
	public int bus_busroad_resv_all_count(int departure, int arrival) {
		return dao.bus_busroad_resv_all_count(departure, arrival);
	}
}
