package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Hotel;
import com.example.demo.entity.Payment;

import jakarta.transaction.Transactional;
@Repository
public interface PaymentDAO extends JpaRepository<Payment, Integer> {
	@Query(value= "select nvl(max(no),0)+1 from payment", nativeQuery = true)
	public int getNextNo();
	
	@Query(value = "select * from payment order by no", nativeQuery = true)
	public List<Payment> findAllByOrderByNo();
	
	@Query(value = "select * from payment where BUYER_ID=?1 order by no", nativeQuery = true)
	public List<Payment> listPay(String id);
	
	//전체 hotelresv, payment 조인
	@Query(value =  "select * from payment inner join hotelresv on payment.resvno = hotelresv.hotelresvno where buyer_id is not null order by no",nativeQuery = true)
	public List<Map<String, Object>> listPayhotelresv();
	
	//id별 hotel, payment 조인
	@Query(value =  "select * from payment inner join hotelresv on payment.resvno = hotelresv.hotelresvno where buyer_id=?1 order by no",nativeQuery = true)
	public List<Map<String, Object>> listPayhotelresvbyId(String id);
	
	@Query(value =  "select * from payment inner join hotelresv on payment.resvno = hotelresv.hotelresvno where no=?1 order by no",nativeQuery = true)
	public List<Map<String, Object>> listPayhotelresvbyNo(int no);
	
	@Query(value =  "select * from payment inner join hotelresv on payment.resvno = hotelresv.hotelresvno where apply_num=?1 order by no",nativeQuery = true)
	public List<Map<String, Object>> listPayhotelresvbyApplynum(String apply_num);
	
	@Modifying
	@Query(value = "delete from Payment p where p.no=?1", nativeQuery = true)
	@Transactional
	public int delete(int no);
}

