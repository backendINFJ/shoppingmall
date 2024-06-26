package com.sparta.shoppingmall.jwt;

import com.sparta.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String refreshToken;


    /**
     * 토큰 생성자
     */
    public RefreshToken(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }

    /**
     * 토큰 업데이트
     */
    public void update(String newToken) {
        this.refreshToken = newToken;
    }


}