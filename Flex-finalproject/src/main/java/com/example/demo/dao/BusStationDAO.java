package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bus;
import com.example.demo.entity.BusStation;

import jakarta.transaction.Transactional;
@Repository
public interface BusStationDAO extends JpaRepository<BusStation, Integer> {
	
	@Query(value = "select * from Busstation order by stationno", nativeQuery = true)
	public List<BusStation> findAllByOrderByStationno();
	
	@Query(value = "select nvl(max(stationno),0)+1 from busstation", nativeQuery = true)
	public int getNextStationno();

	@Modifying
	@Query(value = "delete from Busstation b where b.stationno=?1", nativeQuery = true)
	@Transactional
	public int deleteBusstation(int stationno);
	
	@Query(value = "select * from (select rownum rn,A.* from (select * from busstation order by stationno)A) where rn between ?1 and ?2",nativeQuery = true)
	public List<BusStation> busstation_list_count(int start, int end);

	//역번호로 버스정보가져오기
	@Query(value =  "select * from busstation where stationno=?1",nativeQuery = true)
	public BusStation getBusStation_no(int stationno);
	
	//검색어찾기->키워드로받아서 개수몇개인지받아오기
	//★if문 어떻게 처리하는지 몰라
	@Query(value="select * from busstation where stationname like CONCAT(CONCAT('%', ?1), '%')", nativeQuery=true)
	public List<BusStation> find_station(String keyword);

//	//★되는지확인해야★검색어찾기->역이름쳐서 정보가져오기->다시가져와야해(if문처리x, 그뒤에추가쿼리처리x)
//	@Query(value =  "select * from busstation where stationname=?1",nativeQuery = true)
//	public List<BusStation> findStation (int pageNum,String keyword);
	

}
