package com.sparta.shoppingmall.product.entity;

import com.sparta.shoppingmall.base.entity.Timestamped;
import com.sparta.shoppingmall.comment.entity.Comment;
import com.sparta.shoppingmall.order.entity.OrderGroup;
import com.sparta.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //상품명

    @Column(nullable = false)
    private Long price; //가격

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status; //상태

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_group_id")
    private OrderGroup orderGroup;

    /**
     * 생성자
     */
    @Builder
    public Product(String name, Long price, ProductStatus status, User user){
        this.user = user;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    /**
     * 상품 정보 업데이트
     */
    public void update(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    /**
     * 상품 상태 수정
     */
    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    /**
     * 상품 상태 확인
     */
    public boolean checkProductStatus() {
         return switch(this.status) {
            case RECOMMAND -> true;
            case ONSALE -> true;
            case COMPLETED -> false;
            case INPROGRESS -> false;
        };
    }

    /**
     * 좋아요 갯수 증가
     */
    public void increaseLikeCount() {
        this.likeCount++;
    }

    /**
     * 좋아요 갯수 감소
     */
    public void decreaseLikeCount() {
        this.likeCount--;
    }

}