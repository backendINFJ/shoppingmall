package com.sparta.shoppingmall.jwt;


import com.sparta.shoppingmall.user.entity.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "JwtProvider")
@Component
@RequiredArgsConstructor
public class JwtProvider {
    //refreshToken Service
    private final RefreshTokenService refreshTokenService;

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 리프레시 헤더 값
    public static final String REFRESH_HEADER = "RefreshToken";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEAR = "Bearer ";
    // 토큰 만료시간 (30분)
    public static final long TOKEN_TIME = 30 * 60 * 1000L;
    // 리프레시 토큰 만료시간 (7일)
    public static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;

    // JWT secret key
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(accessKeyBytes);
    }

    // 토큰 생성
    public String createToken(String username, long expiredTime, UserType role) {
        Date date = new Date();

        return BEAR + Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + expiredTime))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // header에서 JWT value 가져오기
    public String getJwtFromHeader(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith(BEAR)) {
            return bearerToken.substring(BEAR.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateTokenInternal(String token, String type) {
        try {
            if("refresh".equals(type)){
                Claims info = getUsernameFromClaims(token);

                RefreshToken refreshToken = refreshTokenService.findByUsername(info.getSubject()).orElse(null);

                if(refreshToken == null) {
                    return false;
                }else {
                    if(!token.equals(refreshToken.getRefreshToken())) {
                        return false;
                    }
                }
            }
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 Claims 불러오기
    public Claims getUsernameFromClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
