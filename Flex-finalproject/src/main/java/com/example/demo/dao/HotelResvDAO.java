package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusResv;
import com.example.demo.entity.HotelResv;

import jakarta.transaction.Transactional;
@Repository
public interface HotelResvDAO extends JpaRepository<HotelResv, Integer> {
	
	@Query(value = "select * from hotelresv order by hotelresvno", nativeQuery = true)
	public List<HotelResv> findAllByOrderByHotelresvno();
	
	@Query(value = "select * from hotelresv where hotelno=?1", nativeQuery = true)
	public List<HotelResv> ADlistHotelresv(int hotelno);
	
	@Query(value = "select * from hotelresv where id=?1 order by hotelresvno",nativeQuery = true)
	public List<HotelResv> hotelresv(String id);
	
	@Query(value = "select nvl(max(hotelresvno),0)+1 from hotelresv", nativeQuery = true)
	public int getNextHotelresvno();

	@Modifying
	@Query(value = "delete from hotelresv h where h.hotelresvno=?1", nativeQuery = true)
	@Transactional
	public int delete(int hotelresvno);

}
