package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.BoardsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardsLikeRepository extends JpaRepository<BoardsLike, Long> {

    BoardsLike findByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardsLike> findAllByBoardIdAndLikeState(Long boardId, boolean likeState);
}
