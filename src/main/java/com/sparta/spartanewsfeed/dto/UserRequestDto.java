package com.sparta.spartanewsfeed.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$",
            message = "PW는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다.")
    private String password;
    private String name;
    private String address;
}
