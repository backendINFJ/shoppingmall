package com.sparta.shoppingmall.cart.dto;

import com.sparta.shoppingmall.cart.entity.CartProduct;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CartResponse {

    private final Long cartId;
    private final Page<CartProduct> cartProducts;

    @Builder
    public CartResponse(Long id, Page<CartProduct> cartProducts) {
        this.cartId = id;
        this.cartProducts = cartProducts;
    }

}
