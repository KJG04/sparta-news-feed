package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardsRepository extends JpaRepository<Boards, Long> {
    List<Boards> findAllByUserIdIn(List<Long> userList);
}
