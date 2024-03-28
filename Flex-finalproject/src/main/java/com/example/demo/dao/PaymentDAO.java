package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Payment;
@Repository
public interface PaymentDAO extends JpaRepository<Payment, Integer> {
	@Query(value= "select nvl(max(no),0)+1 from payment", nativeQuery = true)
	public int getNextNo();
}

