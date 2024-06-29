package com.sparta.shoppingmall.user.dto;

import com.sparta.shoppingmall.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDTO {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String address;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;


    public SignupResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
    }
}
