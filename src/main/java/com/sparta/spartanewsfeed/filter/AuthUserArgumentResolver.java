package com.sparta.spartanewsfeed.filter;

import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.jwt.JwtUtil;
import com.sparta.spartanewsfeed.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j(topic = "AuthUserArgumentResolver")
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthUserArgumentResolver(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String tokenValue = jwtUtil.getTokenFromRequest(request);

        if (StringUtils.hasText(tokenValue)) {
            String token = jwtUtil.substringToken(tokenValue);

            Claims info = jwtUtil.getUserInfoFromToken(token);
            log.info("사용자 정보 확인 " + info.toString());
            return userRepository.findById(Long.valueOf(info.getSubject()))
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        }
        throw new IllegalArgumentException("토큰이 존재하지 않습니다!");
    }
}
