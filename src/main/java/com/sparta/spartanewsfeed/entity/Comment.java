package com.sparta.spartanewsfeed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id")
    Boards boards;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "comment")
    List<CommentLike> likes;

    @Builder
    public Comment(String contents, Boards boards, User user, List<CommentLike> likes) {
        this.contents = contents;
        this.boards = boards;
        this.user = user;
        this.likes = likes;
    }
}
