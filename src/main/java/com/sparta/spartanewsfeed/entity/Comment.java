package com.sparta.spartanewsfeed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String contents;

    @ManyToOne
    @JoinColumn(name = "board_id")
    Boards boards;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Builder
    public Comment(String contents, Boards boards, User user) {
        this.contents = contents;
        this.boards = boards;
        this.user = user;
    }
}
