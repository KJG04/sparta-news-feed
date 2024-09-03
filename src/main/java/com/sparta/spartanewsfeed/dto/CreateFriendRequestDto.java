package com.sparta.spartanewsfeed.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateFriendRequestDto {
    Long userId;
}
