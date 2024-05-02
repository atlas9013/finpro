package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusBusRoad;
import com.example.demo.entity.BusResv;
import com.example.demo.entity.BusResv_BusRoad;

import jakarta.transaction.Transactional;
@Repository
public interface BusResvDAO extends JpaRepository<BusResv, Integer> {
	
	@Query(value = "select * from busresv order by resvno", nativeQuery = true)
	public List<BusResv> findAllByOrderByResvno();
	
	@Query(value = "select nvl(max(resvno),0)+1 from busresv", nativeQuery = true)
	public int getNextResvno();

	@Modifying
	@Query(value = "delete from busresv b where b.resvno=?1", nativeQuery = true)
	@Transactional
	public int delete(int resvno);
	
	@Query(value = "select * from (select rownum rn,A.* from (select * from busresv order by resvno)A) where rn between ?1 and ?2",nativeQuery = true)
	public List<BusResv> busresv_list_count(int start, int end);

	@Query(value = "select busresv.id,busresv.busno,seat,usepoint,savepoint,resvdate,busresv.price,arrival,departure,arrtime,tottime,busresv.resvno,busresv.roadno from busresv inner join busroad on busresv.roadno=busroad.roadno where busresv.id=?1",nativeQuery = true)
	public List<Map<String, Object>> resvlist(String id);
	
	@Query(value = "select *from busresv where resvdate=?1 and roadno=?2",nativeQuery = true)
	public List<BusResv> list_seat_resv_user(String resvdate, int roadno);

	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3",nativeQuery = true)
	public List<Map<String, Object>> listDispatch_resv(int arrival, int departure, String grade);

	//listDispatch_resv_count 만 쓰게 되면 삭제
	@Query(value = "select bus.busno, grade, seat, busroad.id, roadno, arrival, departure, price, arrtime, tottime from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and busroad.departure=?2 and grade=?3", nativeQuery = true)
	public List<Map<String, Object>> list_seat_resv_user(int arrival, int departure, String grade);
	
	//5개씩 list 출력하기위해 일반,우등 둘중에하나(출발시간 빠른순으로)
	@Query(value = "select * from (select rownum rn,A.* from (select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3 order by arrtime asc)A) where rn between ?4 and ?5",nativeQuery = true)
	public List<BusBusRoad> listDispatch_resv_count(int arrival, int departure, String grade, int start, int end);
	
	//5개씩 list 출력하기위해 일반,우등 둘중에하나(출발시간 빠른순으로) 출발지 도착지 반대로
	@Query(value = "select * from (select rownum rn,A.* from (select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3 order by arrtime asc)A) where rn between ?4 and ?5",nativeQuery = true)
	public List<BusBusRoad> listDispatch_resv_reverse_count(int departure, int arrival, String grade, int start, int end);
	
	@Query(value = "select * from (select rownum rn,A.* from (select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3 order by arrtime asc)A)",nativeQuery = true)
	public List<Map<String, Object>> listDispatch_resv_reverse_count1(int departure, int arrival, String grade);
	
	@Query(value = "select roadno,grade,arrival,departure,grade,price,arrtime,tottime,seat from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3",nativeQuery = true)
	public List<BusBusRoad> listDispatch_resv_reverse(int departure, int arrival, String grade);
	
	@Query(value = "select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2",nativeQuery = true)
	public List<Map<String, Object>> listDispatch_resv_all(int arrival, int departure);
	
	@Query(value = "select * from (select rownum rn,A.* from (select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 order by arrtime asc)A) where rn between ?3 and ?4",nativeQuery = true)
	public List<BusBusRoad> listDispatch_resv_all_count(int arrival, int departure, int start, int end);
	
	@Query(value = "select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2",nativeQuery = true)
	public List<Map<String, Object>> listDispatch_resv_all_reverse(int departure, int arrival);
	
	//5개씩 list 출력하기위해 일반 우등 둘다 (출발시간 빠른순으로) 출발지 도착지 반대로
	@Query(value = "select * from (select rownum rn,A.* from (select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 order by arrtime asc)A) where rn between ?3 and ?4",nativeQuery = true)
	public List<BusBusRoad> listDispatch_resv_all_reverse_count(int departure, int arrival, int start, int end);
	
	@Query(value = "select * from bus inner join busroad on bus.busno=busroad.busno where roadno=?1",nativeQuery = true)
	public Map<String, Object> resv_user_seat_select(int roadno);
	
	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3",nativeQuery = true)
	public int bus_busroad_resv_count(int arrival, int departure, String grade);
	
	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3",nativeQuery = true)
	public int bus_busroad_resv_resverse_count(int departure, int arrival, String grade);
	
	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2",nativeQuery = true)
	public int bus_busroad_resv_resverse_all_count(int departure, int arrival);
	
	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2",nativeQuery = true)
	public int bus_busroad_resv_all_count(int departure, int arrival);
}
