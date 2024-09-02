package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.UserDeleteRequestDto;
import com.sparta.spartanewsfeed.dto.UserModifyRequestDto;
import com.sparta.spartanewsfeed.dto.UserResponseDto;
import com.sparta.spartanewsfeed.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
@Log4j2
public class UserInfoController {
    private final UserService userService;

    /*
     * 유저 수정
     *
     * @param {userId}
     * @return UserResponseDto
     * */
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> modify(@PathVariable("userId") Long userId, @RequestBody UserModifyRequestDto userModifyRequestDto, HttpServletRequest request) {
        UserResponseDto userResponseDto = userService.modify(userId, userModifyRequestDto, request);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    /*
     * 유저 삭제
     *
     * @param userId
     * @return String
     * */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId, @RequestBody UserDeleteRequestDto userDeleteRequestDto, HttpServletRequest request) {
        userService.delete(userId, userDeleteRequestDto, request);
        return new ResponseEntity<>("성공적으로 삭제가 되었습니다.", HttpStatus.OK);
    }
}
