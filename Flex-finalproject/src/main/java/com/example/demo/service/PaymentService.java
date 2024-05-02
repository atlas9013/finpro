package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PaymentDAO;
import com.example.demo.entity.Payment;

import lombok.Setter;

@Service
@Setter
public class PaymentService {
   @Autowired
   private PaymentDAO dao;
   
   public void insert(Payment p) {
      p.setNo(dao.getNextNo());
      dao.save(p);
   }
   
   public List<Payment> findAllByOrderByNo(){
	   return dao.findAllByOrderByNo();
   }
   
   public List<Payment> listPay(String id){
	   return dao.listPay(id);
   }
   
   public List<Map<String, Object>> listPayhotelresv(){
	   return dao.listPayhotelresv();
   }
   
   public List<Map<String, Object>> listPayhotelresvbyId(String id){
	   return dao.listPayhotelresvbyId(id);
   }
   
   public List<Map<String, Object>> listPayhotelresvbyNo(int no){
	   return dao.listPayhotelresvbyNo(no);
   }
   
   public List<Map<String, Object>> listPayhotelresvbyApplynum(String apply_num){
	   return dao.listPayhotelresvbyApplynum(apply_num);
   }
   
   public int delete(int no){
		return dao.delete(no);
	}
}