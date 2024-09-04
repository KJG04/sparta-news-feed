package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.entity.Comment;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.repository.BoardsRepository;
import com.sparta.spartanewsfeed.repository.CommentRepository;
import com.sparta.spartanewsfeed.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {
    final CommentRepository commentRepository;
    final UserRepository userRepository;
    final BoardsRepository boardsRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, BoardsRepository boardsRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.boardsRepository = boardsRepository;
    }

    @Transactional
    public List<Comment> getAllComments() {
        // 삭제되지 않은 유저의 댓글만
        return commentRepository.getAllByUser_DeleteStatus(false);
    }

    @Transactional
    public Comment addComment(Long userId, Long boardId, String contents) {
        User user = userRepository.findByUserIdAndDeleteStatus(userId, false).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "userId에 해당하는 User가 없습니다."));
        Boards boards = boardsRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "boardId에 해당하는 Board가 없습니다."));
        Comment comment = Comment.builder().contents(contents).user(user).boards(boards).build();
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
