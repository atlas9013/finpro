package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BusDAO;
import com.example.demo.dao.BusRoadDAO;
import com.example.demo.entity.Bus;
import com.example.demo.entity.BusRoad;

import lombok.Setter;

@Service
@Setter
public class BusroadService {
	@Autowired
	private BusRoadDAO dao;

	public List<BusRoad> listBusroad(){
		return dao.listBusRoad();
	}
	public List<BusRoad> busroad_list_count(int start,int end){
		return dao.busroad_list_count(start,end);
	}
	public void insert(BusRoad b) {
		b.setRoadno(dao.getNextRoadno());
		dao.save(b);
	}
	public int deleteBusroad(int roadno){
		return dao.deleteBusroad(roadno);
	}
	public BusRoad getBusroad(int roadno) {
		return dao.findById(roadno).get();
	}
	public void updateBusroad(BusRoad b){
		dao.save(b);
	}
	public int count() {
		return (int)dao.count();
	}
	public int getBusRoadByRoadno(int roadno) {
		return dao.getBusRoadByRoadno(roadno);
	}
}
