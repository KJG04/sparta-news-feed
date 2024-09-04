package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.Friend;
import com.sparta.spartanewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromUser_UserIdAndToUser_UserId(Long fromUser_userId, Long toUser_userId);

    List<Friend> findAllByFromUser_UserIdAndToUser_DeleteStatus(Long fromUser_userId, Boolean toUser_deleteStatus);

    // 특정 유저가 요청한 모든 친구 관계 찾기
    List<Friend> findAllByFromUser(User fromUser);

    // 특정 유저가 수락한 모든 친구 관계 찾기
    List<Friend> findAllByToUser(User toUser);
}
