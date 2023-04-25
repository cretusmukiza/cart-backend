package com.cretus.retailbackend.repository;

import com.cretus.retailbackend.ShoppingCartStatus;
import com.cretus.retailbackend.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository  extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByStatus(ShoppingCartStatus status);
}
