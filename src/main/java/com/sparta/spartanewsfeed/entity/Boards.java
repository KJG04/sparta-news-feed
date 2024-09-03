package com.sparta.spartanewsfeed.entity;

import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Boards extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "contents", length = 500)
    private String contents;

    @Column(name = "userId")
    private Long userId;

    public Boards(BoardsRequestDto boardsRequestDto, User user) {
        this.contents = boardsRequestDto.getContents();
        this.userId = user.getUserId();
    }

    public Boards update(BoardsRequestDto boardsRequestDto){
        contents = boardsRequestDto.getContents();
        return this;
    }
}
