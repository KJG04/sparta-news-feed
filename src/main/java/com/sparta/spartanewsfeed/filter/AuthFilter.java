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

                // 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                // 나머지 API 요청은 인증 처리 진행
                // 토큰 확인
                String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

                if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                    // JWT 토큰 substring
                    String token = jwtUtil.substringToken(tokenValue);

                    // 토큰 검증
                    if (!jwtUtil.validateToken(token)) {
                        throw new IllegalArgumentException("Token Error");
                    }

                    // 토큰에서 사용자 정보 가져오기
                    Claims info = jwtUtil.getUserInfoFromToken(token);

                    log.info("사용자 정보 확인 " + info.toString());

                    User user = userRepository.findById(Long.valueOf(info.getSubject())).orElseThrow(() ->
                            new NullPointerException("사용자를 찾을 수 없습니다.")
                    );
                    log.info("사용자 정보 확인 " + user);


                    request.setAttribute("user", user);
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
