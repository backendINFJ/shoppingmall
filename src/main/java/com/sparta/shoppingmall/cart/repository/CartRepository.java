package com.sparta.shoppingmall.cart.repository;

import com.sparta.shoppingmall.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long id);
}
