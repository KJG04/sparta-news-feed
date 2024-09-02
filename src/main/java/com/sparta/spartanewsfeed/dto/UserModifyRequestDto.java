package com.sparta.spartanewsfeed.dto;

import lombok.Data;

@Data
public class UserModifyRequestDto {
    private String password;
    private String newPassword;
    private String name;
    private String address;
}
