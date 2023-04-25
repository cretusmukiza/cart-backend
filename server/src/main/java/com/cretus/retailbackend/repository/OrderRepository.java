package com.cretus.retailbackend.repository;

import com.cretus.retailbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { }
