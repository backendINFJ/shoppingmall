package com.sparta.shoppingmall.user.dto;

import com.sparta.shoppingmall.user.entity.User;
import com.sparta.shoppingmall.user.entity.UserStatus;
import com.sparta.shoppingmall.user.entity.UserType;
import lombok.Getter;

@Getter
public class AdminUserResponse {

    private final String username;
    private final String password;
    private final String recentPassword;
    private final String recentPassword2;
    private final String recentPassword3;
    private final String name;
    private final String email;
    private final String address;
    private final UserType userType;
    private final UserStatus userStatus;

    public AdminUserResponse(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.recentPassword = user.getRecentPassword();
        this.recentPassword2 = user.getRecentPassword2();
        this.recentPassword3 = user.getRecentPassword3();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.userType = user.getUserType();
        this.userStatus = user.getUserStatus();
    }

}
