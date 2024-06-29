package com.sparta.shoppingmall.like.repository;

import com.sparta.shoppingmall.like.entity.ContentType;
import com.sparta.shoppingmall.like.entity.LikeStatus;
import com.sparta.shoppingmall.like.entity.Likes;
import com.sparta.shoppingmall.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByContentTypeAndContentId(ContentType contentType, Long contentId);

    int countByContentIdAndContentType(Long contentId, ContentType contentType);

    Page<Likes> findByUserAndStatus(User user, LikeStatus status, Pageable pageable); // 1
}
