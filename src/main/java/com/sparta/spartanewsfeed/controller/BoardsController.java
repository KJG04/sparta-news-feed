package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.service.BoardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("")
    public BoardsResponseDto createBoard(@RequestBody BoardsRequestDto boardsRequestDto) {
        return boardsService.createBoard(boardsRequestDto);
    }

    @GetMapping("/{boardId}")
    public BoardsResponseDto getOneBoard(@PathVariable("boardId") Long boardId) {
        return boardsService.getOneBoard(boardId);
    }

    @GetMapping("")
    public List<BoardsResponseDto> getAllBoards() {
        return boardsService.getAllBoards();
    }

    @PatchMapping("/{boardId}")
    public BoardsResponseDto patchBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardsRequestDto boardsRequestDto) {
        return boardsService.patchBoard(boardId, boardsRequestDto);
    }

    @DeleteMapping("/{boardId}")
    public Long deleteBoard(@PathVariable("boardId") Long boardId) {
        return boardsService.deleteBoard(boardId);
    }
}
