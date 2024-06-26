package com.sparta.shoppingmall.order.repository;

import com.sparta.shoppingmall.order.entity.OrderGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {

    Optional<List<OrderGroup>> findByUserId(long userId);
}
