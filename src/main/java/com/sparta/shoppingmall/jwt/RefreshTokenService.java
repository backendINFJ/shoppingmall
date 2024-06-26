package com.sparta.shoppingmall.jwt;

import com.sparta.shoppingmall.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    @Transactional
    public void saveRefreshToken(User user, String refreshToken) {
        Optional<RefreshToken> existToken = findByUsername(user.getUsername());

        if (existToken.isPresent()) {
            existToken.get().update(refreshToken);
        } else {
            refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken));
        }
    }

    @Transactional
    public void deleteToken(String username) {
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자의 refresh Token이 존재하지 않습니다.")
        );
        refreshTokenRepository.delete(refreshToken);
    }
}