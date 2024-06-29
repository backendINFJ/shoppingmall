package com.sparta.shoppingmall.user.dto;

import com.sparta.shoppingmall.user.entity.UserStatus;
import com.sparta.shoppingmall.user.entity.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminUpdateUserRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotBlank(message = "최근 첫번째 비밀번호를 입력해 주세요.")
    private String recentPassword;

    @NotBlank(message = "최근 두번째 비밀번호를 입력해 주세요.")
    private String recentPassword2;

    @NotBlank(message = "최근 세번째 비밀번호를 입력해 주세요.")
    private String recentPassword3;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "회원의 권한을 설정하세요.")
    private UserType userType;

    @NotBlank(message = "회원 상태를 입력해주세요.")
    private UserStatus userStatus;

}
