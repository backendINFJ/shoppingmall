package com.sparta.shoppingmall.order.entity;

import com.sparta.shoppingmall.base.entity.Timestamped;
import com.sparta.shoppingmall.product.entity.Product;
import com.sparta.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "order_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderGroup extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "orderGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orderGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products = new ArrayList<>();

    /**
     * 생성자
     */
    @Builder
    public OrderGroup (String address, Long totalPrice, OrderStatus status, User user, List<Product> products) {
        this.address = address;
        this.totalPrice = totalPrice;
        this.status = status;
        this.user = user;
        this.products = products;
    }

    /**
     * 주문 상태 업데이트
     */
    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.products = new ArrayList<>();
    }

    /**
     * 상품 정보 삭제하기
     */
    public void removeProduct(Product product) {
        if (this.products.contains(product)) {
            this.products.remove(product);
            product.setOrderGroup(null); // 연관 관계 업데이트
        }
    }

    /**
     * List<Order>에 Order추가하기
     */
    public void addOrder(Orders orders) {
        this.orders.add(orders);
    }

    /**
     * 해당 주문의 사용자 확인
     */
    public void verifyOrderGroupUser(Long userId) {
        if (!userId.equals(this.user.getId())) {
           throw new IllegalArgumentException("주문자와 사용자가 일치하지 않습니다.");
        }
    }

}
