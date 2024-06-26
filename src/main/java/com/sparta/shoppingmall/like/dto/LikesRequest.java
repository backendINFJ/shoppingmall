package com.sparta.shoppingmall.like.dto;

import com.sparta.shoppingmall.like.entity.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikesRequest {

    @NotNull(message = "컨텐츠 유형을 입력하세요")
    private ContentType contentType;

    @NotNull(message = "컨텐츠 id 값이 없습니다.")
    private Long contentId;

}
