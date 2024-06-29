package com.sparta.shoppingmall.user.entity;

public enum UserType {

    USER, // 일반 사용자
    ADMIN; // 관리자

    public String getAuthority() {
        switch (this) { // 사용자 분류
            case USER:
                return "USER"; // 일반 사용자
            case ADMIN:
                return "ADMIN"; // 관리자
            default:
                throw new IllegalArgumentException("해당 권한은 존재하지 않습니다.");
        }
    }
}
