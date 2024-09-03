package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.BoardOneResponseDto;
import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.service.BoardsService;
import com.sparta.spartanewsfeed.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("")
    public BoardsResponseDto createBoard(@RequestBody BoardsRequestDto boardsRequestDto, User user) {
        return boardsService.createBoard(boardsRequestDto, user);
    }

    @GetMapping("/{boardId}")
    public BoardOneResponseDto getOneBoard(@PathVariable("boardId") Long boardId) {
        return boardsService.getOneBoard(boardId);
    }

    @GetMapping("")
    public List<BoardsResponseDto> getAllBoards() {
        return boardsService.getAllBoards();
    }

    @PatchMapping("/{boardId}")
    public BoardsResponseDto patchBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardsRequestDto boardsRequestDto, User user) {
        return boardsService.patchBoard(boardId, boardsRequestDto, user);
    }

    @DeleteMapping("/{boardId}")
    public Long deleteBoard(@PathVariable("boardId") Long boardId, User user) {
        return boardsService.deleteBoard(boardId, user);
    }
}
