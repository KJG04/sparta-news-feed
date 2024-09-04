package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.Boards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardsRepository extends JpaRepository<Boards, Long> {

    Page<Boards> findAllByUserIdIn(Pageable pageable, List<Long> userList);


    // 특정 유저가 작성한 모든 게시글 찾기
    List<Boards> findAllByUserId(Long userId);
}
