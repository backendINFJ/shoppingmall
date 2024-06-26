package com.sparta.shoppingmall.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EditPasswordRequestDTO {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,15}$", message = "password: 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    private String newPassword;

}
