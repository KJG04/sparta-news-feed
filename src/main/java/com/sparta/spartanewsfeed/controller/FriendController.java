package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.service.FriendService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
public class FriendController {
    final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

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
