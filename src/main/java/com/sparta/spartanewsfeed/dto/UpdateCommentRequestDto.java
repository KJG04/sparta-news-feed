package com.sparta.spartanewsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCommentRequestDto {
    @NotEmpty
    String contents;
}
