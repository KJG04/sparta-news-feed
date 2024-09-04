package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByComment_IdAndUser_UserId(Long comment_id, Long user_userId);
}
