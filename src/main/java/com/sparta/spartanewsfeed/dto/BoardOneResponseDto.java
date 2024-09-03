package com.sparta.spartanewsfeed.dto;

import com.sparta.spartanewsfeed.entity.Boards;
import lombok.Getter;

@Getter
public class BoardOneResponseDto {
    private Long boardId;
    private String contents;
    private int likeCount;
    private Long userId;

    public BoardOneResponseDto(Boards board, int likeCount) {
        userId = board.getUserId();
        boardId = board.getBoardId();
        contents = board.getContents();
        this.likeCount = likeCount;
    }

}
