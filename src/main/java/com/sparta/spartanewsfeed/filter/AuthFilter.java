package com.sparta.spartanewsfeed.filter;

import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.jwt.JwtUtil;
import com.sparta.spartanewsfeed.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
@Order(2)
@RequiredArgsConstructor
public class AuthFilter implements Filter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = httpServletRequest.getRequestURI();

        try {
            if (url.startsWith("/users") || url.startsWith("/users/login")) {
                // /users , /users/login으로 시작하는 url이면 해당 요청은 인증할 필요없이 넘어간다.
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                // 나머지 API 요청은 인증 처리 진행
                // 토큰 확인
                String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

                if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                    // JWT 토큰 substring, Bearer 접두사를 제거하여 실제 토큰을 얻는다.
                    String token = jwtUtil.substringToken(tokenValue);

                    // 토큰 검증
                    if (!jwtUtil.validateToken(token)) {
                        throw new IllegalArgumentException("Token Error");
                    }

                    chain.doFilter(request, response); // 다음 Filter 로 이동
                } else {
                    throw new JwtException("토큰이 존재하지 않습니다!");
                }
            }
        } catch (JwtException e) {
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
