package com.sparta.spartanewsfeed.dto;

import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardOneResponseDto {
    private Long boardId;
    private String contents;
    private int likeCount;
    private Long userId;
    private List<CommentResponseDto> commentResponseDtos;

    public BoardOneResponseDto(Boards board, List<Comment> comments) {
        userId = board.getUserId();
        boardId = board.getBoardId();
        contents = board.getContents();
        likeCount = board.getLikeCount();

        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(v ->
                        CommentResponseDto.builder()
                                .id(v.getId())
                                .contents(v.getContents())
                                .user(new UserResponseDto(v.getUser()))
                                .likeCount((long) v.getLikes().size())
                                .build()
                ).toList();
        this.commentResponseDtos = commentResponseDtos;
    }

}
