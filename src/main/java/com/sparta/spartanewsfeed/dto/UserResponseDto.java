package com.sparta.spartanewsfeed.dto;

import com.sparta.spartanewsfeed.entity.User;
import lombok.*;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private String name;
    private String address;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.address = user.getAddress();
    }
}
