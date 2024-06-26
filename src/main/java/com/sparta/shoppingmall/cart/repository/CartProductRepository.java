package com.sparta.shoppingmall.cart.repository;

import com.sparta.shoppingmall.cart.entity.CartProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    Optional<CartProduct> findByProductId(Long productId);

    Page<CartProduct> findAllByCartId(Long cartId, Pageable pageable);

}
