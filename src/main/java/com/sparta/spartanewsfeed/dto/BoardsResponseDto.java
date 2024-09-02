package com.sparta.spartanewsfeed.dto;

import com.sparta.spartanewsfeed.entity.Boards;
import lombok.Getter;

@Getter
public class BoardsResponseDto {
    private Long boardId;
    private String contents;

    public BoardsResponseDto(Boards board) {
        boardId = board.getBoardId();
        contents = board.getContents();
    }
}
