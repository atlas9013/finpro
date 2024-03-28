package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusBusRoad;
import com.example.demo.entity.BusRoad;

import jakarta.transaction.Transactional;
@Repository
public interface Bus_BusRoadDAO extends JpaRepository<BusBusRoad, Integer> {
	
//	//5개씩 출력(반대로)
//	@Query(value = "select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2",nativeQuery = true)
//	public List<Bus_BusRoad> listResv_all_reverse(String departure, String arrival);
//
//	@Query(value = "select * from (select rownum rn,A.* from (select * from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 order by arrtime asc)A) where rn between ?3 and ?4",nativeQuery = true)
//	public List<Bus_BusRoad> listResv_all_reverse_count(String departure, String arrival, int start, int end);
//
//	//노선번호에따라 다 보여줘~
//	@Query(value = "select * from bus inner join busroad on bus.busno=busroad.busno where roadno=?1",nativeQuery = true)
//	public List<Bus_BusRoad> resv_user_seat_select(int roadno);
//	
//	//[버스, 버스노선]전체레코드수를 반환하는 메소드를 정의  
//	//bus 와 bus_road join 한테이블(출발지 도착지 등급 에 맞추어 )행의개수 파악(출발지 도착지 바꾸어서)
//	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2 and grade=?3",nativeQuery = true)
//	public int resverse_count(String departure, String arrival, String grade);
//	
//	// bus 와 busroad join 한테이블(출발지 도착지 모든등급 에 맞추어 )행의개수 파악(출발지 도착지 바꾸어서)
//	@Query(value = "select count(*) from bus inner join busroad on bus.busno=busroad.busno where busroad.arrival=?1 and departure=?2",nativeQuery = true)
//	public int resverse_all_count(String departure, String arrival);
//	
	//사용중인 busno 제외하고 출력
	@Query(value = "select A.busno,grade from bus A left outer join busroad B on A.busno=B.busno where B.busno is null",nativeQuery = true)
	public List<BusBusRoad> bus_no_list_null();
	
	//사용중인 bus_no 제외하고 카운트출력
	@Query(value =  "select count(A.busno) from bus A left outer join busroad B on A.busno=B.busno where B.busno is null",nativeQuery = true)
	public int bus_no_list_null_count();
	
	//사용중이지 않은 bus_no 결과의 첫번째행
	@Query(value =  "select A.busno from bus A left outer join busroad B on A.busno=B.busno where B.busno is null and rownum=1",nativeQuery = true)
	public BusBusRoad bus_no_null_rownum();
}
