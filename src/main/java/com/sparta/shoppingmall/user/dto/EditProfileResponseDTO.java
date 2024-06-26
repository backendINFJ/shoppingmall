package com.sparta.shoppingmall.user.dto;


import com.sparta.shoppingmall.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EditProfileResponseDTO {

    private final Long id;

    private final String username;

    private final String email;

    private final String address;

    private final LocalDateTime createAt;

    private final LocalDateTime updateAt;


    public EditProfileResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
    }
}
