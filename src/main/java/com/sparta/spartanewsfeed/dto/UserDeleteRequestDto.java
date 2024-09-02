package com.sparta.spartanewsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDeleteRequestDto {
    @NotBlank
    private String password;
}
