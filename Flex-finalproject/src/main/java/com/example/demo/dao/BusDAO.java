package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bus;

import jakarta.transaction.Transactional;
@Repository
public interface BusDAO extends JpaRepository<Bus, Integer> {
	
	@Query(value = "select * from Bus order by busno", nativeQuery = true)
	public List<Bus> findAllByOrderByBusno();
	
	@Query(value = "select nvl(max(busno),0)+1 from bus", nativeQuery = true)
	public int getNextBusno();

	@Modifying
	@Query(value = "delete from Bus b where b.busno=?1", nativeQuery = true)
	@Transactional
	public int deleteBus(int busno);
	
	@Query(value = "select * from (select rownum rn,A.* from (select * from bus order by busno)A) where rn between ?1 and ?2",nativeQuery = true)
	public List<Bus> bus_list_count(int start, int end);

	@Modifying
	@Query(value = "insert into bus values(bus_seq.nextval,'일반',30)",nativeQuery = true)
	@Transactional
	public Bus insertBus_normal();
}
