package com.example.demo.service;

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
}