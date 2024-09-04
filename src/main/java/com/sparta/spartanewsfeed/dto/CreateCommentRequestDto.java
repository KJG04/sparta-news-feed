package com.sparta.spartanewsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCommentRequestDto {
    @NotNull
    Long boardId;

    @NotEmpty
    String contents;
}
