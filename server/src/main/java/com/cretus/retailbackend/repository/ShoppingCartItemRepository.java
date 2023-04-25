package com.cretus.retailbackend.repository;

import com.cretus.retailbackend.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

}
