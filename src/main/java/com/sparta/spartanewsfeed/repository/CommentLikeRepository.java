package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByComment_IdAndUser_UserId(Long comment_id, Long user_userId);

    // 특정 유저가 누른 모든 댓글 좋아요 정보 조회
    @Query("SELECT c FROM CommentLike c WHERE c.user.userId = :userId")
    List<CommentLike> findAllByUser_UserId(@Param("userId") Long userId);
}
