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

//    @Transactional
//    public BoardsLikeResponseDto boardsLike(Long boardId, User user) {
//        Boards board = boardsRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다."));
//        board.addLike();
//        BoardsLike boardsLike = new BoardsLike(user.getUserId(), boardId, true);
//
//        // 이미 좋아요를 누른적이 있는지 확인하기
//        List<BoardsLike> boardsLikesList = boardsLikeRepository.findAllByBoardIdAAndLikeState(boardId, true);
//        List<BoardsLike> boardsUnLikesList = boardsLikeRepository.findAllByBoardIdAAndLikeState(boardId, false);
//        List<Long> userIdList = new ArrayList<>();
//        List<Long> userIdUnLikeList = new ArrayList<>();
//        for (BoardsLike like : boardsLikesList) {
//            userIdList.add(like.getUserId());
//        }
//        for (BoardsLike unlike : boardsUnLikesList) {
//            userIdList.add(unlike.getUserId());
//        }
//        if(userIdList.contains(user.getUserId())) {
//            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
//        } else if(userIdUnLikeList.contains(user.getUserId())){
//            return new BoardsLikeResponseDto(boardsLike.update());
//        }else {
//            BoardsLike saveBoardsLike = boardsLikeRepository.save(boardsLike);
//            return new BoardsLikeResponseDto(saveBoardsLike);
//        }
//    }

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
