package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.BoardsLike;
import com.sparta.spartanewsfeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getAllByUser_DeleteStatus(Boolean user_deleteStatus);

    // 특정 게시글에 대한 모든 댓글 찾기
    List<Comment> findAllByBoards_BoardId(Long boardId);

    // 특정 유저의 댓글 찾기
    List<Comment> findAllByUser_UserId(Long userId);
}
