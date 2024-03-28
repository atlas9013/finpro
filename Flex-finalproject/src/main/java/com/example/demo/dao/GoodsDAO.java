package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Goods;

@Repository
public interface GoodsDAO extends JpaRepository<Goods, Integer> {

}

