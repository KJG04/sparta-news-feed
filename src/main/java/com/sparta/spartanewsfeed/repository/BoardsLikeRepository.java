package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.BoardsLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardsLikeRepository extends JpaRepository<BoardsLike, Long> {

    BoardsLike findByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardsLike> findAllByBoardIdAndLikeState(Long boardId, boolean likeState);

    // 특정 게시글에 대한 모든 좋아요 찾기
    @Query("SELECT b FROM BoardsLike b WHERE b.boardId =:boardId")
    List<BoardsLike> findAllByBoardId(@Param("boardId") Long boardId);

    // 특정 유저가 누른 모든 좋아요 찾기
    @Query("SELECT b FROM BoardsLike b WHERE b.userId = :userId")
    List<BoardsLike> findAllByUserId(@Param("userId") Long userId);
}
