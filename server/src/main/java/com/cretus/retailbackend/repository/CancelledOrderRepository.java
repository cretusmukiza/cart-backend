package com.cretus.retailbackend.repository;

import com.cretus.retailbackend.entity.CancelOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelledOrderRepository extends JpaRepository<CancelOrder, Long> {
}
