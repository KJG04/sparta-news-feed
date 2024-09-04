package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.entity.Comment;
import com.sparta.spartanewsfeed.entity.CommentLike;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.repository.CommentLikeRepository;
import com.sparta.spartanewsfeed.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentLikeService {
    final CommentLikeRepository commentLikeRepository;
    final CommentRepository commentRepository;

    public CommentLikeService(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void likeComment(User user, Long commentId) {
        if (commentLikeRepository.findByComment_IdAndUser_UserId(commentId, user.getUserId()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 좋아요를 했습니다.");

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "commentId에 해당하는 Comment가 없습니다."));

        if (comment.getUser().getUserId().equals(user.getUserId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 댓글에는 좋아요 할 수 없습니다.");

        CommentLike commentLike = CommentLike.builder().comment(comment).user(user).build();
        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void unLikeComment(User user, Long commentId) {
        CommentLike commentLike = commentLikeRepository.findByComment_IdAndUser_UserId(commentId, user.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "좋아요 상태가 아닙니다."));
        commentLikeRepository.delete(commentLike);
    }
}
