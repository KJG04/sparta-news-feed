package com.sparta.spartanewsfeed.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @GetMapping()
    void getAllComments() {

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
