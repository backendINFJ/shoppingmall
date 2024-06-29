package com.sparta.shoppingmall.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.shoppingmall.base.dto.CommonResponse;
import com.sparta.shoppingmall.jwt.JwtProvider;
import com.sparta.shoppingmall.user.entity.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

import static com.sparta.shoppingmall.jwt.JwtProvider.*;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 토큰 검증
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.getJwtFromHeader(req, AUTHORIZATION_HEADER);
        String refreshToken = jwtProvider.getJwtFromHeader(req, REFRESH_HEADER);

        if (StringUtils.hasText(accessToken)) {
            if(!jwtProvider.validateTokenInternal(accessToken, "access")) {
                if (StringUtils.hasText(refreshToken)) {
                    log.info("Access Token 검증 실패 && Refresh Token 존재");

                    if (jwtProvider.validateTokenInternal(refreshToken, "refresh")) {
                        log.info("Refresh Token 검증 성공 && Access Token 재생성");

                        Claims info = jwtProvider.getUsernameFromClaims(refreshToken);
                        String newAccessToken = jwtProvider.createToken(info.getSubject(), TOKEN_TIME, info.get(AUTHORIZATION_KEY, UserType.class));
                        res.setHeader(AUTHORIZATION_HEADER, newAccessToken);
                        setAuthentication(info.getSubject());

                    } else {
                        log.info("Refresh Token 검증 실패 && 재로그인 요청");

                        writeJsonResponse(res, HttpStatus.UNAUTHORIZED, "Refresh Token 검증 실패. 재로그인 해주세요.", "");
                        return;
                    }
                }
            } else {
                log.info("Access Token 검증 성공");

                Claims info = jwtProvider.getUsernameFromClaims(accessToken);
                setAuthentication(info.getSubject());
            }
        }

        filterChain.doFilter(req, res);
    }

    /**
     * 인증 처리
     */
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * HttpResponse write Json Response
     */
    private void writeJsonResponse(HttpServletResponse response, HttpStatus status, String message, String data) throws IOException {
        CommonResponse responseMessage = CommonResponse.<String>builder()
                .statusCode(status.value())
                .message(message)
                .data(data)
                .build();

        String jsonResponse = new ObjectMapper().writeValueAsString(responseMessage);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }
}