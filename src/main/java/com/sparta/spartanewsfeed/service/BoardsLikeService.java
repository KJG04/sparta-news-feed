package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.BoardsLikeResponseDto;
import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.entity.BoardsLike;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.repository.BoardsLikeRepository;
import com.sparta.spartanewsfeed.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsLikeService {

    private final BoardsLikeRepository boardsLikeRepository;
    private final BoardsRepository boardsRepository;

    @Transactional
    public BoardsLikeResponseDto boardsLike(Long boardId, User user) {
        BoardsLike boardsLike = boardsLikeRepository.findByBoardIdAndUserId(boardId, user.getUserId());
        // 처음 좋아요를 누르는 경우 생성을 하고, 만약 전에 누른적이 있다면 상태를 변경합니다.
        if (boardsLike == null) {
            BoardsLike newboardsLike = new BoardsLike(user.getUserId(), boardId, true);
            BoardsLike saveBoardsLike = boardsLikeRepository.save(newboardsLike);
            return new BoardsLikeResponseDto(saveBoardsLike);
        } else {
            if(boardsLike.getLikeState()) {
                return new BoardsLikeResponseDto(boardsLike.update());
            } else{
                return new BoardsLikeResponseDto(boardsLike.update());
            }
        }
    }


}
