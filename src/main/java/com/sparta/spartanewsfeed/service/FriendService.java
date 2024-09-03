package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.entity.Friend;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.repository.FriendRepository;
import com.sparta.spartanewsfeed.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FriendService {
    final FriendRepository friendRepository;
    final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    Friend addFriend(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "fromUserId에 해당하는 User가 없습니다."));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "toUserId에 해당하는 User가 없습니다."));
        Friend friend = Friend.builder().fromUser(fromUser).toUser(toUser).build();
        return friendRepository.save(friend);
    }

    @Transactional
    void deleteFriend(Long fromUserId, Long toUserId) {
        Friend friend = friendRepository.findByFromUser_UserIdAndToUser_UserId(fromUserId, toUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "fromUser와 toUser가 친구 상태가 아닙니다."));
        friendRepository.delete(friend);
    }

    @Transactional
    List<Friend> getFriendsByFromUserId(Long fromUserId) {
        return friendRepository.findAllByFromUser_UserId(fromUserId);
    }
}
