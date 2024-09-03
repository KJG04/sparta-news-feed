package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.BoardsLikeResponseDto;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.BoardsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boardslike")
@RequiredArgsConstructor
public class BoardsLikeContorller {

    private final BoardsLikeService boardsLikeService;

    @PostMapping("/{boardId}")
    public BoardsLikeResponseDto boardsLike(@PathVariable("boardId") Long boardId, User user) {
        return boardsLikeService.boardsLike(boardId, user);
    }

}
