package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BusDAO;
import com.example.demo.entity.Bus;
import lombok.Setter;

@Service
@Setter
public class BusService {
	@Autowired
	private BusDAO dao;

	public List<Bus> listBus(){
		return dao.findAllByOrderByBusno();
	}
	
	public List<Bus> ADlistBus(String id){
		return dao.ADlistBus(id);
	}
	
	public List<Bus> bus_list_count(int start,int end){
		return dao.bus_list_count(start,end);
	}
	public void insert(Bus b) {
		b.setBusno(dao.getNextBusno());
		dao.save(b);
	}
	public int deleteBus(int busno){
		return dao.deleteBus(busno);
	}
	public Bus getBus(int busno) {
		return dao.findById(busno).get();
	}
	public void updateBus(Bus b){
		dao.save(b);
	}
	public int count() {
		return (int)dao.count();
	}
	public int insertBus_normal() {
		return dao.insertBus_normal();
	}
	
}
