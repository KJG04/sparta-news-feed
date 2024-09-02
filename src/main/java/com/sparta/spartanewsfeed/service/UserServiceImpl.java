package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.*;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.exception.NotFoundException;
import com.sparta.spartanewsfeed.exception.PasswordErrorException;
import com.sparta.spartanewsfeed.jwt.JwtUtil;
import com.sparta.spartanewsfeed.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Transactional
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        User user = new User(userRequestDto);
        user.setPassword(password);
        User saveUser = userRepository.save(user);
        return new UserResponseDto(saveUser);
    }

    @Transactional
    @Override
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse res) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordErrorException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUserId(), user.getName());

        jwtUtil.addJwtToCookie(token, res); // 쿠키를 HTTP 응답에 추가
        return token;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));
        return new UserResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto modify(Long userId, UserModifyRequestDto userModifyRequestDto, HttpServletRequest request) {
        // exceptin 검사는 AuthFilter에서 진행됨
        // token값을 해석해서 info에 있는 subject값
        // 즉 토큰의 userId값과 매개변수로 온 userId값이 일치하는지 확인
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        String token = jwtUtil.substringToken(tokenValue);
        Claims info = jwtUtil.getUserInfoFromToken(token);
        if(!userId.equals(Long.valueOf(info.getSubject()))) {
            throw new SecurityException("해당 유저에 대한 엑세스 권한이 없습니다!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));

        // 비밀번호 교체에 대한 요청이 있을 시에
        if(userModifyRequestDto.getPassword() != null) {
            String password = userModifyRequestDto.getPassword();
            String newPassword = passwordEncoder.encode(userModifyRequestDto.getNewPassword());
            // 비밀번호 교체
            if(passwordEncoder.matches(password, user.getPassword())) {
                user.changePassword(newPassword);
            } else {
                throw new PasswordErrorException("비밀번호가 일치하지 않습니다.");
            }
        }
        // 이름 교체
        if(userModifyRequestDto.getName() != null) {
            user.changeName(userModifyRequestDto.getName());
        }
        // 주소 교체
        if(userModifyRequestDto.getAddress() != null) {
            user.changeAddress(userModifyRequestDto.getAddress());
        }
        return new UserResponseDto(user);
    }

    @Transactional
    @Override
    public void delete(Long userId, UserDeleteRequestDto userDeleteRequestDto, HttpServletRequest request) {
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        String token = jwtUtil.substringToken(tokenValue);
        Claims info = jwtUtil.getUserInfoFromToken(token);
        if(!userId.equals(Long.valueOf(info.getSubject()))) {
            throw new SecurityException("해당 유저에 대한 엑세스 권한이 없습니다!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 회원이 없습니다."));
        String password = userDeleteRequestDto.getPassword();
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordErrorException("비밀번호가 일치하지 않습니다.");
        }
        user.setDeleteStatus(true);
    }
}
