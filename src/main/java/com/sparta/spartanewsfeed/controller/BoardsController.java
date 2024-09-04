package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.BoardOneResponseDto;
import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.entity.Friend;
import com.sparta.spartanewsfeed.service.BoardsService;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;
    private final FriendService friendService;

    @PostMapping("")
    public BoardsResponseDto createBoard(@RequestBody BoardsRequestDto boardsRequestDto, User user) {
        return boardsService.createBoard(boardsRequestDto, user);
    }

    @GetMapping("/{boardId}")
    public BoardOneResponseDto getOneBoard(@PathVariable("boardId") Long boardId) {
        return boardsService.getOneBoard(boardId);
    }

    @GetMapping("")
    public List<BoardsResponseDto> getAllBoards(User user) {
        List<Friend> friendList = friendService.getFriendsByFromUserId(user.getUserId());
        return boardsService.getAllBoards(friendList, user);
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
