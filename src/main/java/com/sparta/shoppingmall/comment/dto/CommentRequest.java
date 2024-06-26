package com.sparta.shoppingmall.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}
