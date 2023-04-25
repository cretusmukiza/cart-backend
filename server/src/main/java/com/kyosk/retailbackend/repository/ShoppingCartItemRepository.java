package com.kyosk.retailbackend.repository;

import com.kyosk.retailbackend.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {

}
