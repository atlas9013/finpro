package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.BusStation;
import com.example.demo.entity.Hotel;

import jakarta.transaction.Transactional;
@Repository
public interface HotelDAO extends JpaRepository<Hotel, Integer> {
	
	@Query(value = "select * from hotel order by hotelno desc", nativeQuery = true)
	public List<Hotel> findAllByOrderByHotelnoDesc();
	
	@Query(value = "select * from hotel where id=?1 order by hotelno", nativeQuery = true)
	public List<Hotel> ADlistHotel(String id);
	
	@Query(value = "select * from hotel where address like CONCAT(CONCAT('%', ?1), '%')", nativeQuery = true)
	public List<Hotel> listHoteladdress(String address);
	
	@Query(value = "select nvl(max(hotelno),0)+1 from hotel", nativeQuery = true)
	public int getNextHotelno();

	@Modifying
	@Query(value = "delete from hotel h where h.hotelno=?1", nativeQuery = true)
	@Transactional
	public int delete(int hotelno);
	
	@Query(value = "select * from (select rownum rn,A.* from (select * from hotel order by hotelno)A) where rn between ?1 and ?2",nativeQuery = true)
	public List<Hotel> hotel_list_count(int start, int end);
	
	//검색어찾기
	@Query(value = "select * from (select rownum rn,A.* from (select * from hotel where name like CONCAT(CONCAT('%', ?1), '%') order by hotelno)A) where rn between ?2 and ?3",nativeQuery = true)
	public List<Hotel> find_hotel(String keyword, int start, int end);

	@Query(value = "select count(*) from hotel where name like CONCAT(CONCAT('%', ?1), '%')", nativeQuery = true)
	public int find_hotel_count(String keyword);
	
	//검색어찾기
	@Query(value = "select * from hotel where name like CONCAT(CONCAT('%', ?1), '%') order by hotelno",nativeQuery = true)
	public List<Hotel> find_hotelbyname(String keyword);
}
