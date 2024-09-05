package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.BoardOneResponseDto;
import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.entity.Friend;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.BoardsService;
import com.sparta.spartanewsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;
    private final FriendService friendService;

    @PostMapping("")
    public ResponseEntity<BoardsResponseDto> createBoard(@RequestBody BoardsRequestDto boardsRequestDto, User user) {
        BoardsResponseDto boardsResponseDto = boardsService.createBoard(boardsRequestDto, user);
        return new ResponseEntity<>(boardsResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardOneResponseDto> getOneBoard(@PathVariable("boardId") Long boardId, User user) {
        List<Friend> friendList = friendService.getFriendsByFromUserId(user.getUserId());
        BoardOneResponseDto boardOneResponseDto = boardsService.getOneBoard(boardId, friendList, user);
        return new ResponseEntity<>(boardOneResponseDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<BoardsResponseDto>> getAllBoards(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                @RequestParam(name = "size", defaultValue = "10") int size,
                                                                @RequestParam(name = "sortBy",defaultValue = "createAt") String sortBy,
                                                                @RequestParam(name = "isAsc", defaultValue = "false") boolean isAsc,
                                                                User user) {
        List<Friend> friendList = friendService.getFriendsByFromUserId(user.getUserId());
        Page<BoardsResponseDto> boardsResponseDtoList = boardsService.getAllBoards(page-1, size, sortBy, isAsc, friendList, user);
        return new ResponseEntity<>(boardsResponseDtoList, HttpStatus.OK);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardsResponseDto> patchBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardsRequestDto boardsRequestDto, User user) {
        BoardsResponseDto boardsResponseDto = boardsService.patchBoard(boardId, boardsRequestDto, user);
        return new ResponseEntity<>(boardsResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId, User user) {
        boardsService.deleteBoard(boardId, user);
        return new ResponseEntity<>("성공적으로 삭제가 되었습니다.", HttpStatus.OK);
    }
}
