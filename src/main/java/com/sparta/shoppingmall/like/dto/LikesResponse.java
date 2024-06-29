package com.sparta.shoppingmall.like.dto;

import com.sparta.shoppingmall.like.entity.ContentType;
import com.sparta.shoppingmall.like.entity.LikeStatus;
import com.sparta.shoppingmall.like.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LikesResponse {

    private final Long id;
    private final ContentType contentType;
    private final Long contentId;
    private final LikeStatus status;

    public LikesResponse(Likes likes) {
        this.id = likes.getId();
        this.contentType = likes.getContentType();
        this.contentId = likes.getContentId();
        this.status = likes.getStatus();
    }

}
