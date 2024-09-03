package com.sparta.spartanewsfeed.dto;

import com.sparta.spartanewsfeed.entity.BoardsLike;
import lombok.Getter;

@Getter
public class BoardsLikeResponseDto {
    private Long boardsLikeId;
    private Long boardId;
    private Boolean likeState;

    public BoardsLikeResponseDto(BoardsLike boardsLike) {
        this.boardsLikeId = boardsLike.getBoardsLikeId();
        this.boardId = boardsLike.getBoardId();
        this.likeState = boardsLike.getLikeState();
    }
}
