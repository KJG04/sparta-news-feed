package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;

    public BoardsResponseDto createBoard(BoardsRequestDto boardsRequestDto) {
        Boards boards = new Boards(boardsRequestDto);
        Boards saveBoards = boardsRepository.save(boards);
        return new BoardsResponseDto(saveBoards);
    }

    public BoardsResponseDto getOneBoard(Long boardId) {
        return new BoardsResponseDto(getOneBoardWithId(boardId));
    }

    public List<BoardsResponseDto> getAllBoards() {
        return boardsRepository.findAll().stream().map(BoardsResponseDto::new).toList();
    }

    @Transactional
    public BoardsResponseDto patchBoard(Long boardId, BoardsRequestDto boardsRequestDto) {
        Boards board = getOneBoardWithId(boardId);
        return new BoardsResponseDto(board.update(boardsRequestDto));
    }

    @Transactional
    public Long deleteBoard(Long boardId) {
        boardsRepository.delete(getOneBoardWithId(boardId));
        return boardId;
    }

    private Boards getOneBoardWithId(Long boardId) {
        return boardsRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다."));
    }
}
