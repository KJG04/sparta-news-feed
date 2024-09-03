package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromUser_UserIdAndToUser_UserId(Long fromUser_userId, Long toUser_userId);

    List<Friend> findAllByFromUser_UserId(Long fromUser_userId);
}
