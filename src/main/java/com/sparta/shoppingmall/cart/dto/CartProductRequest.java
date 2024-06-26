package com.sparta.shoppingmall.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartProductRequest {

    @NotNull(message = "상품 아이디는 필수 값 입니다.")
    private Long productId;

}
