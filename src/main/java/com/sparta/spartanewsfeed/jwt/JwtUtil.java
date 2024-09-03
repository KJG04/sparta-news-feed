package com.sparta.spartanewsfeed.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000; // 1시간

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    /*
    * secretKey를 디코딩하여 JWT 서명에 사용할 Key를 초기화한다.
    * secretKey는 Base64로 인코딩된 문자열이므로 이를 디코딩 후
    * Keys.hmacShaKeyFor 메서드를 통해 Key 객체 생성
    * */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /*
    * userId와 name을 토큰에 집어넣는다.
    * .claim(AUTHORIZATION_KEY, "USER") 여기에는 권한 정보를 저장하는 데 사용되므로 Role이 없는 현 상태는 USER를 직접 넣는다.
    * sub 클레임에는 문자열 형태를 요구하므로 userId값을 String 타입으로 변환하여 저장한다.
    * */
    public String createToken(Long userId, String name) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) // 사용자 식별자값(ID)
                        .claim("name", name)
                        .claim(AUTHORIZATION_KEY, "USER") // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();  // JWT 토큰 문자열 생성
    }

    // 생성된 JWT를 Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            // Cookie Value 에는 공백이 불가능해서 encoding 진행
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            // 쿠키 객체를 생성하고 JWT 토큰을 값으로 생성한다.
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            // 해당 쿠키가 모든 경로에 대해서 유효하도록 설정한다.
            cookie.setPath("/");
            // 응답에 쿠키를 추가한다. -> POSTMAN에서 로그인 테스트 실행 시 header에 자동으로 Cookie가 생성되는게 이것 때문
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    /*
    * JWT 토큰 substring
    * Bearer 접두어 포함된 토큰 문자열에서 접두어를 제거하고 JWT 토큰을 반환한다.
    * */
    public String substringToken(String tokenValue) {
        /*
        * StringUtils.hasText(tokenValue) : tokenValue가 null이 아니고 빈 문자열이 아닌지 확인
        * tokenValue.startsWith(BEARER_PREFIX) : 토큰에 Bearer로 시작되는지 확인
        * 조건 만족 시 Bearer를 제거한 나머지 부분을 반환
        * */
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    /*
    * 토큰 검증
    * */
    public boolean validateToken(String token) {
        try {
            /*
            * 주어진 토큰이 유효한지 검증하고, 서명이 올바른지 확인한다.
            * 유효하지 않은 경우 예외처리를 거친다.
            * */
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw e;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new IllegalArgumentException("유효하지 않은 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new IllegalArgumentException("잘못된 JWT 토큰입니다.");
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        /*
        * 서명 검증을 수행한 후, 토큰의 본문(Claims)을 추출한다.
        * 해당 본문에는 사용자 식별자('sub'), 이름('name'), 권한('auth') 등의 정보가 포함된다.
        * */
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /*
    * HttpServletRequest 에서 Cookie Value : JWT 가져오기
    * HTTP 요청에서 JWT 토큰을 포함하는 쿠키를 찾아 반환한다.
    * */
    public String getTokenFromRequest(HttpServletRequest req) {
        // req에서 모든 쿠키를 가져온다.
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                // 쿠키 값에 AUTHORIZATION_HEADER 값이 포함되어 있는 쿠키를 찾는다.
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
