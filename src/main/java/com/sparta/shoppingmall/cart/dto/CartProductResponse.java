package com.sparta.shoppingmall.cart.dto;

import com.sparta.shoppingmall.cart.entity.Cart;
import com.sparta.shoppingmall.product.entity.Product;
import lombok.Getter;

@Getter
public class CartProductResponse {

    private final Long id;
    private final Cart cart;
    private final Product product;

    public CartProductResponse(Long id, Cart cart, Product product) {
        this.id = id;
        this.cart = cart;
        this.product = product;
    }

}
