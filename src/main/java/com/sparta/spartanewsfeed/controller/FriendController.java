package com.sparta.spartanewsfeed.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
public class FriendController {
    @GetMapping
    void getFriends() {
    }

    @PostMapping
    void addFriend() {
    }

    @DeleteMapping("/{userId}")
    void deleteFriend(@PathVariable Long userId) {
    }
}
