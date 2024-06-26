package com.sparta.shoppingmall.product.dto;

import com.sparta.shoppingmall.product.entity.ProductStatus;
import com.sparta.shoppingmall.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long id;
    private final Long userId;
    private final String username;
    private final String name;
    private final Long price;
    private final ProductStatus status;

    @Builder
    public ProductResponse(Long id, String name, Long price, ProductStatus status, User user) {
        this.id = id;
        this.userId = user.getId();
        this.username = user.getUsername(); //사용자 ID
        this.name = name;
        this.price = price;
        this.status = status;
    }
}