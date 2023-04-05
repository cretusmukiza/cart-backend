package com.kyosk.retailbackend.repository;

import com.kyosk.retailbackend.entity.CancelOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelledOrderRepository extends JpaRepository<CancelOrder, Long> {
}
