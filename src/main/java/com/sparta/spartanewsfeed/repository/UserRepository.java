package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.deleteStatus = false")
    Optional<User> getUser(@Param("userId") Long userId);

    Optional<User> findByUserIdAndDeleteStatus(Long userId, Boolean deleteStatus);

    boolean existsByEmail(String email);
}
