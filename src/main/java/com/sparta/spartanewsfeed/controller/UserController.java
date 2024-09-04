package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.LoginRequestDto;
import com.sparta.spartanewsfeed.dto.UserRequestDto;
import com.sparta.spartanewsfeed.dto.UserResponseDto;
import com.sparta.spartanewsfeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    /*
     * 유저 등록
     *
     * @param UserRequestDto
     * @return UserResponseDto
     * */
    @PostMapping()
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.save(userRequestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    /*
     * 로그인
     *
     * @param LoginRequestDto
     * @return 로그인 성공 or 실패 사유
     * */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try{
            String token = userService.login(loginRequestDto, response);
            return new ResponseEntity<>("로그인 성공"+token, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /*
     * 유저 조회
     *
     * @param userId
     * @return UserResponseDto
     * */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> get(@PathVariable("userId") Long userId) {
        UserResponseDto responseDto = userService.get(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
