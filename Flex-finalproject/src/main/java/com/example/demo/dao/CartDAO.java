package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Cart;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> {
   
   @Query(value = "select nvl(max(no),0)+1 from cart", nativeQuery = true)
   public int getNextNo();
   
   //아이디와 상품번호에 해당하는 cart를 조회하여 반환하는 쿼리메소드
   public Cart findByIdAndGno(String id, int gno);
   
   //장바구니 목록 반환
   public List<Cart> findById(String id);
}




