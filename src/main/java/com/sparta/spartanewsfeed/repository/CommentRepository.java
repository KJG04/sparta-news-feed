package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.BoardsLike;
import com.sparta.spartanewsfeed.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"likes"})
    List<Comment> getAllByUser_DeleteStatus(Boolean user_deleteStatus);

    // 특정 게시글에 대한 모든 댓글 찾기
    @Query("SELECT c FROM Comment c WHERE c.boards.boardId =:boardId")
    List<Comment> findAllByBoards_BoardId(@Param("boardId") Long boardId);

    // 특정 유저의 댓글 찾기
    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId")
    List<Comment> findAllByUser_UserId(@Param("userId") Long userId);
}
