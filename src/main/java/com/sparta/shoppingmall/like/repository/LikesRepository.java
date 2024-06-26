package com.sparta.shoppingmall.like.repository;

import com.sparta.shoppingmall.like.entity.ContentType;
import com.sparta.shoppingmall.like.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByContentTypeAndContentId(ContentType contentType, Long contentId);
}
