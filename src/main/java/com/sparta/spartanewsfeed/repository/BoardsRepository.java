package com.sparta.spartanewsfeed.repository;

import com.sparta.spartanewsfeed.entity.Boards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardsRepository extends JpaRepository<Boards, Long> {

    // userList에 있는 user를 모두 찾기
    Page<Boards> findAllByUserIdIn(Pageable pageable, List<Long> userList);

    // userList에 있는 user 중 날짜에 맞는 조건만 찾기
    @Query("select b from Boards b where b.userId In :userList AND b.createAt BETWEEN :startDate AND :endDate")
    Page<Boards> findAllByUserIdAndDate(Pageable pageable, @Param("userList") List<Long> userList, @Param("startDate") LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);


    // 특정 유저가 작성한 모든 게시글 찾기
    List<Boards> findAllByUserId(Long userId);
}
