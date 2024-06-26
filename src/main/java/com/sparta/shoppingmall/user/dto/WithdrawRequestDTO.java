package com.sparta.shoppingmall.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawRequestDTO {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
