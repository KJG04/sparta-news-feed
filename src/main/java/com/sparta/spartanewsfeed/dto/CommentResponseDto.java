package com.sparta.spartanewsfeed.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    Long id;
    String contents;
    UserResponseDto userResponseDto;
}
