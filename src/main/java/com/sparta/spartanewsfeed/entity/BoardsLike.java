package com.sparta.spartanewsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class BoardsLike extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardsLikeId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "likeState", nullable = false)
    private Boolean likeState;

    @Column(name = "boardId", nullable = false)
    private Long boardId;

    public BoardsLike(Long userId, Long boardId, boolean likeState) {
        this.userId = userId;
        this.boardId = boardId;
        this.likeState = likeState;
    }

    public BoardsLike update() {
        likeState = !likeState;
        return this;
    }
}
