package com.sparta.shoppingmall.comment.entity;

import com.sparta.shoppingmall.base.entity.Timestamped;
import com.sparta.shoppingmall.comment.dto.CommentRequest;
import com.sparta.shoppingmall.product.entity.Product;
import com.sparta.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    /**
     * 전체 생성자
     */
    @Builder
    public Comment(Long id, String content, int likeCount, User user, Product product) {
        this.id = id;
        this.content = content;
        this.likeCount = likeCount;
        this.user = user;
        this.product = product;
    }

    /**
     * 생성자
     */
    public Comment(CommentRequest request, User user, Product product) {
        this.content = request.getContent();
        this.user = user;
        this.product = product;
    }

    /**
     * 댓글 수정
     */
    public void updateComment(CommentRequest request) {
        this.content = request.getContent();
    }

    /**
     * 해당 사용자 확인
     */
    public void verifyCommentUser(Long userId) {
        if(!this.user.getId().equals(userId)){
            throw new IllegalArgumentException("해당 사용자의 댓글이 아닙니다.");
        }
    }

    /**
     * 해당 상품 확인
     */
    public void verifyCommentProduct(Long productId) {
        if (!this.product.getId().equals(productId)) {
            throw new IllegalArgumentException("해당 상품의 댓글이 아닙니다.");
        }
    }

    /**
     * 좋아요 수 증가
     */
    public void increaseLikeCount() {
        this.likeCount++;
    }

    /**
     * 좋아요 수 감소
     */
    public void decreaseLikeCount() {
        this.likeCount--;
    }

}
