package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Bus;
import com.example.demo.entity.Hotelboard;

import jakarta.transaction.Transactional;
@Repository
public interface HotelBoardDAO extends JpaRepository<Hotelboard, Integer> {
	
	@Query(value = "select * from (select rownum rn, A.* from (select * from hotelboard where relevel=0 and hotelno=?1 order by regroup desc, restep asc)A) where rn between ?2 and ?3", nativeQuery = true)
	public List<Hotelboard> listHotelboard(int hotelno, int start, int end);
	
	@Query(value = "select * from (select rownum rn, A.* from (select * from hotelboard where relevel>0 and regroup=?1 order by regroup desc, restep asc)A) where rn between ?2 and ?3", nativeQuery = true)
	public List<Hotelboard> listHotelboard2(int regroup, int start, int end);
	
	@Query(value = "select count(*) from hotelboard where relevel=0", nativeQuery = true)
	public int hotelboardcount();
	
	@Query(value = "select count(*) from hotelboard where relevel!=0 and regroup=?1", nativeQuery = true)
	public int hotelboardcount2(int regroup);
	
	@Query(value = "select count(*) from hotelboard where relevel=0 and hotelno=?1", nativeQuery = true)
	public int hotelboardcountbyHotelno(int hotelno);
	
	//가장 최근 댓글달린 regdate출력
	@Query(value = "select regdate from (select regdate from hotelboard where hotelno=?1 order by regdate desc) where rownum = 1", nativeQuery = true)
	public String hotelboardregdatebyHotelno(int hotelno);
	
	@Query(value = "select nvl(max(hotelboardno),0)+1 from Hotelboard", nativeQuery = true)
	public int getNextHotelboardno();

	@Modifying
	@Query(value = "delete from Hotelboard h where h.hotelboardno=?1", nativeQuery = true)
	@Transactional
	public int delete(int hotelboardno);
	
	@Modifying
	@Query(value = "update hotelboard set restep=restep+1 where restep >?1 and regroup = ?2",nativeQuery = true)
	@Transactional
	public int hotelboard_reUP(int restep, int regroup);
	
	@Modifying
	@Query(value = "update hotelboard set readcount = readcount + 1 where hotelboardno = ?1",nativeQuery = true)
	@Transactional
	public int read_count(int hotelboardno);
}
