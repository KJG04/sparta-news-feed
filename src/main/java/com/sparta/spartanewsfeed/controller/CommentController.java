package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.dto.CreateCommentRequestDto;
import com.sparta.spartanewsfeed.dto.UpdateCommentRequestDto;
import com.sparta.spartanewsfeed.dto.UserResponseDto;
import com.sparta.spartanewsfeed.entity.Comment;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    ResponseEntity<CommentResponseDto> addComment(User user, @RequestBody @Valid CreateCommentRequestDto createCommentRequestDto) {
        Comment comment = commentService.addComment(user.getUserId(), createCommentRequestDto.getBoardId(), createCommentRequestDto.getContents());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommentResponseDto.builder().
                        id(comment.getId())
                        .contents(comment.getContents())
                        .user(new UserResponseDto(comment.getUser()))
                        .likeCount((long) comment.getLikes().size())
                        .build()
        );
    }

    @PatchMapping("/{commentId}")
    ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, User user, @RequestBody @Valid UpdateCommentRequestDto updateCommentRequestDto) {
        Comment comment = commentService.updateComment(user.getUserId(), commentId, updateCommentRequestDto.getContents());
        return ResponseEntity.ok(
                CommentResponseDto.builder().
                        id(comment.getId())
                        .contents(comment.getContents())
                        .user(new UserResponseDto(comment.getUser()))
                        .build()
        );
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity deleteComment(@PathVariable Long commentId, User user) {
        commentService.deleteComment(user.getUserId(), commentId);
        return ResponseEntity.noContent().build();
    }
}
