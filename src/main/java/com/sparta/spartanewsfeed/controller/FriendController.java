package com.sparta.spartanewsfeed.controller;

import com.sparta.spartanewsfeed.dto.CreateFriendRequestDto;
import com.sparta.spartanewsfeed.dto.UserResponseDto;
import com.sparta.spartanewsfeed.entity.Friend;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.service.FriendService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {
    final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    ResponseEntity<List<UserResponseDto>> getFriends(User user) {
        List<Friend> friends = friendService.getFriendsByFromUserId(user.getUserId());
        List<UserResponseDto> toUsers = friends.stream().map(v -> new UserResponseDto(v.getToUser())).toList();
        return ResponseEntity.ok(toUsers);
    }

    @PostMapping
    ResponseEntity addFriend(@RequestBody @Valid CreateFriendRequestDto createFriendRequestDto, User user) {
        friendService.addFriend(user.getUserId(), createFriendRequestDto.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    ResponseEntity deleteFriend(@PathVariable Long userId, User user) {
        friendService.deleteFriend(user.getUserId(), userId);
        return ResponseEntity.noContent().build();
    }
}
