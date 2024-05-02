package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Room;

import jakarta.transaction.Transactional;
@Repository
public interface RoomDAO extends JpaRepository<Room, String> {
	
	@Query(value = "select nvl(max(roomno),0) from room where hotelno = ?1", nativeQuery = true)
	public String maxRoomno(int hotelno);
	
	@Query(value = "select * from room order by roomno", nativeQuery = true)
	public List<Room> findAllByOrderByRoomno();
	
	@Query(value = "select * from room where roomno = ?1", nativeQuery = true)
	public Room getRoom(String roomno);
	
	@Modifying
	@Query(value = "delete from room r where r.roomno=?1", nativeQuery = true)
	@Transactional
	public int delete(String roomno);
	
	@Query(value = "select DISTINCT * from room where hotelno = ?1",nativeQuery = true)
	public List<Room> listRoom2(int hotelno);

}
