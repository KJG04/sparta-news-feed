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
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    /*
    * 주어진 매개변수가 resolver를 통해 처리될 수 있는지를 결정
    * 매개변수가 User타입인 경우에만 resolver가 사용된다.
    * 컨트롤러에 User타입의 매개변수가 있다면 작동
    * */
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
        /*
        * 동작 방식
        * 1. HttpServletRequest 객체를 가져온다.
        * 2. JwtUtil 클래스를 통해 JWT 토큰을 추출한다.
        * 3. 토큰 존재 시 JwtUtil를 통해 파싱하여 Claims 객체를 얻고 그 안의 정보를 추출한다. sub('id'), name('name') 등
        * 4. userRepository를 통해 DB에서 User 객체를 반환한다.
        * */
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
