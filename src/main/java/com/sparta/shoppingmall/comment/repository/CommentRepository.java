package com.sparta.shoppingmall.comment.repository;

import com.sparta.shoppingmall.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {



}
