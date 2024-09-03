package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.BoardOneResponseDto;
import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.entity.BoardsLike;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.repository.BoardsLikeRepository;
import com.sparta.spartanewsfeed.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final BoardsLikeRepository boardsLikeRepository;

    public BoardsResponseDto createBoard(BoardsRequestDto boardsRequestDto, User user) {
        Boards boards = new Boards(boardsRequestDto, user);
        return new BoardsResponseDto(boardsRepository.save(boards));
    }

    public BoardOneResponseDto getOneBoard(Long boardId) {
        List<BoardsLike> boardsLikeList = boardsLikeRepository.findAllByBoardIdAndLikeState(boardId, true);
        return new BoardOneResponseDto(getOneBoardWithId(boardId), boardsLikeList.size());
    }

    public List<BoardsResponseDto> getAllBoards() {
        return boardsRepository.findAll().stream().map(BoardsResponseDto::new).toList();
    }

    @Transactional
    public BoardsResponseDto patchBoard(Long boardId, BoardsRequestDto boardsRequestDto, User user) {
        Boards board = getOneBoardWithId(boardId);
        // 작성자만 글을 수정할 수 있습니다.
        if (board.getUserId().equals(user.getUserId())) {
            return new BoardsResponseDto(board.update(boardsRequestDto));
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        // 작성자만 글을 삭제할 수 있습니다.
        Boards boards = getOneBoardWithId(boardId);
        if (boards.getUserId().equals(user.getUserId())) {
            boardsRepository.delete(getOneBoardWithId(boardId));
            return boardId;
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    private Boards getOneBoardWithId(Long boardId) {
        return boardsRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다."));
    }
}
