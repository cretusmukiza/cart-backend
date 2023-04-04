package com.kyosk.retailbackend.repository;

import com.kyosk.retailbackend.ShoppingCartStatus;
import com.kyosk.retailbackend.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository  extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByStatus(ShoppingCartStatus status);
}
