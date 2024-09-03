package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.*;
import com.sparta.spartanewsfeed.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    // 유저 등록
    UserResponseDto save(UserRequestDto userRequestDto);

    // 유저 로그인
    String login(LoginRequestDto loginRequestDto, HttpServletResponse res);

    // 유저 단건 조회
    UserResponseDto get(Long userId);

    // 유저 수정
    UserResponseDto modify(Long userId, UserModifyRequestDto userModifyRequestDto, User user);

    // 유저 삭제
    void delete(Long userId, UserDeleteRequestDto userDeleteRequestDto, User user);
}
