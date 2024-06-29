package com.sparta.shoppingmall.comment.dto;

import com.sparta.shoppingmall.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private final Long id;

    private final String content;

    private final int likeCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreateAt();
        this.updatedAt = comment.getUpdateAt();
    }

}
