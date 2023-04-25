package com.kyosk.retailbackend.repository;

import com.kyosk.retailbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { }
