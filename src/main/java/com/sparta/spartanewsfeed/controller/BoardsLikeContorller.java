package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.BoardsLikeResponseDto;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.BoardsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like/boards")
@RequiredArgsConstructor
public class BoardsLikeContorller {

    private final BoardsLikeService boardsLikeService;

    @PostMapping("/{boardId}")
    public ResponseEntity<BoardsLikeResponseDto> boardsLike(@PathVariable("boardId") Long boardId, User user) {
        BoardsLikeResponseDto boardsLikeResponseDto = boardsLikeService.boardsLike(boardId, user);
        return new ResponseEntity<>(boardsLikeResponseDto, HttpStatus.OK);
    }

}
