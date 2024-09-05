package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.CommentLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like/comments")
public class CommentLikeController {
    final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    @PostMapping("/{commentId}")
    ResponseEntity likeComment(User user, @PathVariable("commentId") Long commentId) {
        commentLikeService.likeComment(user, commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity unLikeComment(User user, @PathVariable("commentId") Long commentId) {
        commentLikeService.unLikeComment(user, commentId);
        return ResponseEntity.noContent().build();
    }
}
