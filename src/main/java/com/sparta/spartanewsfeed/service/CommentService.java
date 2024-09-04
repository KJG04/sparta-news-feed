package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.entity.Comment;
import com.sparta.spartanewsfeed.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        // 삭제되지 않은 유저의 댓글만
        return commentRepository.getAllByUser_DeleteStatus(false);
    }
}
