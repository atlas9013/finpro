package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.RoomDAO;
import com.example.demo.entity.Room;

import lombok.Setter;

@Service
@Setter
public class RoomService {
	@Autowired
	private RoomDAO dao;

	public List<Room> listRoom(){
		return dao.findAllByOrderByRoomno();
	}
	public List<Room> listRoom2(int hotelno){
		return dao.listRoom2(hotelno);
	}
	
	public void insert(Room r) {
		dao.save(r);
	}
	public int delete(String roomno){
		return dao.delete(roomno);
	}
	public Room getRoom(String roomno) {
		return dao.getRoom(roomno);
	}
	public void update(Room r){
		dao.save(r);
	}
	public int count() {
		return (int)dao.count();
	}
}
