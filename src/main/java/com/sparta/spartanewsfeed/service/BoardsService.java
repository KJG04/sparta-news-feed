package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.BoardOneResponseDto;
import com.sparta.spartanewsfeed.dto.BoardsRequestDto;
import com.sparta.spartanewsfeed.dto.BoardsResponseDto;
import com.sparta.spartanewsfeed.entity.*;
import com.sparta.spartanewsfeed.repository.BoardsLikeRepository;
import com.sparta.spartanewsfeed.repository.BoardsRepository;
import com.sparta.spartanewsfeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final BoardsLikeRepository boardsLikeRepository;
    private final CommentRepository commentRepository;

    public BoardsResponseDto createBoard(BoardsRequestDto boardsRequestDto, User user) {
        Boards boards = new Boards(boardsRequestDto, user);
        return new BoardsResponseDto(boardsRepository.save(boards));
    }

    public BoardOneResponseDto getOneBoard(Long boardId, List<Friend> friendList, User user) {
        List<Long> userList = new ArrayList<>();
        // 자신의 글을 불러오기 위해 로그인 한 사람 id 추가
        userList.add(user.getUserId());
        // 친구들의 글을 불러오기 위해 친구 id 추가
        for (Friend friend : friendList) {
            userList.add(friend.getToUser().getUserId());
        }

        Boards boards = boardsRepository.findById(boardId).orElseThrow();

        if(!userList.contains(boards.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시판의 접근이 불가능합니다.");
        }

        List<BoardsLike> boardsLikeList = boardsLikeRepository.findAllByBoardIdAndLikeState(boardId, true);
        List<Comment> comments = commentRepository.findAllByBoards_BoardId(boardId);
        return new BoardOneResponseDto(getOneBoardWithId(boardId), boardsLikeList.size(), comments);
    }

    public Page<BoardsResponseDto> getAllBoards(int page, int size, String sortBy, boolean isAsc, List<Friend> friendList, User user) {
        // pageing 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Long> userList = new ArrayList<>();
        // 자신의 글을 불러오기 위해 로그인 한 사람 id 추가
        userList.add(user.getUserId());
        // 친구들의 글을 불러오기 위해 친구 id 추가
        for (Friend friend : friendList) {
            userList.add(friend.getToUser().getUserId());
        }

        // 친구의 글을 가져와서 dto에 넣어줌
        Page<BoardsResponseDto> boardsResponseDtoList = boardsRepository.findAllByUserIdIn(pageable, userList).map(BoardsResponseDto::new);

        // 댓글 개수와, 좋아요개수 넣어줌
        for (BoardsResponseDto boardsResponseDto : boardsResponseDtoList) {
            List<BoardsLike> boardsLikeList = boardsLikeRepository.findAllByBoardIdAndLikeState(boardsResponseDto.getBoardId(), true);
            List<Comment> comments = commentRepository.findAllByBoards_BoardId(boardsResponseDto.getBoardId());
            boardsResponseDto.update(boardsLikeList.size(), comments.size());
        }

        return boardsResponseDtoList;
    }

    @Transactional
    public BoardsResponseDto patchBoard(Long boardId, BoardsRequestDto boardsRequestDto, User user) {
        Boards board = getOneBoardWithId(boardId);
        // 작성자만 글을 수정할 수 있습니다.
        if (board.getUserId().equals(user.getUserId())) {
            return new BoardsResponseDto(board.update(boardsRequestDto));
        } else {
            throw new SecurityException("권한이 없습니다.");
        }
    }

    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        // 작성자만 글을 삭제할 수 있습니다.
        Boards boards = getOneBoardWithId(boardId);
        if (boards.getUserId().equals(user.getUserId())) {
            boardsRepository.delete(getOneBoardWithId(boardId));
            return boardId;
        } else {
            throw new SecurityException("권한이 없습니다.");
        }
    }

    private Boards getOneBoardWithId(Long boardId) {
        return boardsRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시판의 ID를 찾을 수 없습니다."));
    }
}
