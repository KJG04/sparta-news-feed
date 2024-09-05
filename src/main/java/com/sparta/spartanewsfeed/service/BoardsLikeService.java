package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.BoardsLikeResponseDto;
import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.entity.BoardsLike;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.repository.BoardsLikeRepository;
import com.sparta.spartanewsfeed.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsLikeService {

    private final BoardsLikeRepository boardsLikeRepository;
    private final BoardsRepository boardsRepository;

    @Transactional
    public BoardsLikeResponseDto boardsLike(Long boardId, User user) {

        BoardsLike boardsLike = boardsLikeRepository.findByBoardIdAndUserId(boardId, user.getUserId());
        Boards board = boardsRepository.findById(boardId).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시판의 ID를 찾을 수 없습니다."));

        // 처음 좋아요를 누르는 경우 생성을 하고, 만약 전에 누른적이 있다면 상태를 변경합니다.
        if(user.getUserId().equals(board.getUserId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 게시글에는 좋아요를 누를 수 없습니다.");
        }

        if (boardsLike == null) {
            BoardsLike newboardsLike = new BoardsLike(user.getUserId(), boardId, true);
            BoardsLike saveBoardsLike = boardsLikeRepository.save(newboardsLike);
            BoardsLikeResponseDto boardsLikeResponseDto = new BoardsLikeResponseDto(saveBoardsLike);
            List<BoardsLike> boardsLikeList = boardsLikeRepository.findAllByBoardIdAndLikeState(boardId, true);
            board.likeUpdate(boardsLikeList.size());
            return boardsLikeResponseDto;
        } else {
            BoardsLikeResponseDto boardsLikeResponseDto = new BoardsLikeResponseDto(boardsLike.update());
            List<BoardsLike> boardsLikeList = boardsLikeRepository.findAllByBoardIdAndLikeState(boardId, true);
            board.likeUpdate(boardsLikeList.size());
            return boardsLikeResponseDto;
        }

    }
}
