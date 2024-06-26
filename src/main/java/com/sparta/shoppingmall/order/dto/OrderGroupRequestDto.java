package com.sparta.shoppingmall.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderGroupRequestDto {

    @NotBlank(message = "배송지는 필수 값입니다.")
    private String address;

    @NotBlank(message = "주문 총 액 필수 값입니다.")
    private Long totalPrice;

    @NotBlank(message = "주문하려는 상품을 선택하세요.")
    private List<Long> productIdList;

}
