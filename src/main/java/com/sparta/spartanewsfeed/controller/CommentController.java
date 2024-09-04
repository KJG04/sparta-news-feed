package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.CommentResponseDto;
import com.sparta.spartanewsfeed.dto.UserResponseDto;
import com.sparta.spartanewsfeed.entity.Comment;
import com.sparta.spartanewsfeed.service.CommentService;
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
                                .userResponseDto(new UserResponseDto(v.getUser()))
                                .build()
                ).toList();
        return ResponseEntity.ok(commentResponseDtos);
    }

    @PostMapping()
    void addComment() {

    }

    @PatchMapping("/{commentId}")
    void updateComment(@PathVariable String commentId) {
    }

    @DeleteMapping("/{commentId}")
    void deleteComment(@PathVariable String commentId) {
    }

}
