package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"likes"})
    List<Comment> getAllByUser_DeleteStatus(Boolean user_deleteStatus);
}
