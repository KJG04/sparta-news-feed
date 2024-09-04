package com.sparta.spartanewsfeed.dto;

import com.sparta.spartanewsfeed.entity.Boards;
import lombok.Getter;

@Getter
public class BoardsResponseDto {
    private Long boardId;
    private String contents;
    private Long userId;
    private int likeCount;
    private int commentCount;

    public BoardsResponseDto(Boards board) {
        boardId = board.getBoardId();
        contents = board.getContents();
        userId = board.getUserId();
    }

    public void update(int likeCount, int commentCount) {
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
