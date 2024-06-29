package com.sparta.shoppingmall.order.repository;

import com.sparta.shoppingmall.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {



}
