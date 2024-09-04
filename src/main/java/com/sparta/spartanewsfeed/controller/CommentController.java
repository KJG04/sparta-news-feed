package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.dto.CreateCommentRequestDto;
import com.sparta.spartanewsfeed.dto.UserResponseDto;
import com.sparta.spartanewsfeed.entity.Comment;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    ResponseEntity<List<CommentResponseDto>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(v ->
                        CommentResponseDto.builder()
                                .id(v.getId())
                                .contents(v.getContents())
                                .user(new UserResponseDto(v.getUser()))
                                .build()
                ).toList();
        return ResponseEntity.ok(commentResponseDtos);
    }

    @PostMapping()
    ResponseEntity<CommentResponseDto> addComment(User user, @RequestBody @Valid CreateCommentRequestDto createCommentRequestDto) {
        Comment comment = commentService.addComment(user.getUserId(), createCommentRequestDto.getBoardId(), createCommentRequestDto.getContents());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommentResponseDto.builder().
                        id(comment.getId())
                        .contents(comment.getContents())
                        .user(new UserResponseDto(comment.getUser()))
                        .build()
        );
    }

    @PatchMapping("/{commentId}")
    void updateComment(@PathVariable Long commentId) {
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
