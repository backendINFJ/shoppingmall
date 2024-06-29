package com.sparta.shoppingmall.user.dto;

import com.sparta.shoppingmall.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDTO {

    private final Long id;

    private final String username;

    private final String email;

    private final String address;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.createdAt = user.getCreateAt();
        this.updatedAt = user.getUpdateAt();
    }
}
