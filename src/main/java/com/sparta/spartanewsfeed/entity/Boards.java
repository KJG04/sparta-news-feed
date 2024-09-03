package com.sparta.spartanewsfeed.entity;

import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "contents", length = 500)
    private String contents;

    @Column(name = "userId")
    private Long userId;

    public Boards(BoardsRequestDto boardsRequestDto) {
        this.contents = boardsRequestDto.getContents();
    }

    public Boards update(BoardsRequestDto boardsRequestDto){
        contents = boardsRequestDto.getContents();
        return this;
    }
}
