package com.sparta.shoppingmall.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EditProfileRequestDTO {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
}
