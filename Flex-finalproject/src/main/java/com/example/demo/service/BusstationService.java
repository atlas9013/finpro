package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BusDAO;
import com.example.demo.dao.BusStationDAO;
import com.example.demo.entity.Bus;
import com.example.demo.entity.BusStation;

import lombok.Setter;

@Service
@Setter
public class BusstationService {
	@Autowired
	private BusStationDAO dao;

	public List<BusStation> listBusstation(){
		return dao.findAllByOrderByStationno();
	}
	public List<BusStation> busstation_list_count(int start,int end){
		return dao.busstation_list_count(start,end);
	}
	public void insert(BusStation b) {
		b.setStationno(dao.getNextStationno());
		dao.save(b);
	}
	public int deleteBusstation(int stationno){
		return dao.deleteBusstation(stationno);
	}
	public BusStation getBusstation(int stationno) {
		return dao.findById(stationno).get();
	}
	public void updateBusstation(BusStation b){
		dao.save(b);
	}
	public int count() {
		return (int)dao.count();
	}
	public BusStation getBusStation_no(int stationno) {
		return dao.getBusStation_no(stationno);
	}
	public List<BusStation> find_station(String keyword) {
		return dao.find_station(keyword);
	}
}
