package com.sparta.shoppingmall.order.dto;

import com.sparta.shoppingmall.order.entity.Orders;
import lombok.Getter;

@Getter
public class OrdersResponse {
    private final Long orderId;
    private final String productName;
    private final Long productPrice;

    public OrdersResponse(Orders order){
        this.orderId = order.getId();
        this.productName = order.getProductName();
        this.productPrice = order.getProductPrice();
    }
}
