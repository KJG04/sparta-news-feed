package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.*;
import com.sparta.spartanewsfeed.entity.*;
import com.sparta.spartanewsfeed.jwt.JwtUtil;
import com.sparta.spartanewsfeed.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BoardsRepository boardsRepository;
    private final BoardsLikeRepository boardsLikeRepository;
    private final FriendRepository friendRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        // email 중복 체크
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "중복된 이메일입니다.");
        }

        String password = passwordEncoder.encode(userRequestDto.getPassword());
        User user = new User(userRequestDto);
        user.setPassword(password);
        User saveUser = userRepository.save(user);
        return new UserResponseDto(saveUser);
    }

    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse res) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 email에 대한 사용자가 없습니다!")
        );
        if(user.getDeleteStatus().equals(true)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 유저입니다!");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다!");
        }

        String token = jwtUtil.createToken(user.getUserId(), user.getName());

        jwtUtil.addJwtToCookie(token, res); // 쿠키를 HTTP 응답에 추가
        return token;
    }

    @Transactional(readOnly = true)
    public UserResponseDto get(Long userId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다!"));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto modify(Long userId, UserModifyRequestDto userModifyRequestDto, User user) {

        if(!userId.equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"해당 유저에 대한 엑세스 권한이 없습니다!");
        }

        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다!"));

        // 비밀번호 교체에 대한 요청이 있을 시에
        if(userModifyRequestDto.getPassword() != null) {
            String password = userModifyRequestDto.getPassword();
            String newPassword = userModifyRequestDto.getNewPassword();
            if(password.equals(newPassword)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 비밀번호로 변경할 수 없습니다!");
            }
            String newPw = passwordEncoder.encode(userModifyRequestDto.getNewPassword());
            // 비밀번호 교체
            if(passwordEncoder.matches(password, newUser.getPassword())) {
                newUser.changePassword(newPw);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다!");
            }
        }
        // 이름 교체
        if(userModifyRequestDto.getName() != null) {
            newUser.changeName(userModifyRequestDto.getName());
        }
        // 주소 교체
        if(userModifyRequestDto.getAddress() != null) {
            newUser.changeAddress(userModifyRequestDto.getAddress());
        }
        return new UserResponseDto(newUser);
    }

    @Transactional
    public void delete(Long userId, UserDeleteRequestDto userDeleteRequestDto, User user) {

        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다!"));

        if(newUser.getDeleteStatus().equals(true)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이미 탈퇴한 유저입니다!");
        }

        if(!userId.equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"해당 유저에 대한 엑세스 권한이 없습니다!");
        }

        String password = userDeleteRequestDto.getPassword();
        if(!passwordEncoder.matches(password, newUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 관련 board 데이터 삭제
        List<Boards> userBoards = boardsRepository.findAllByUserId(userId);
        for(Boards board : userBoards) {
            // 해당 게시글의 좋아요 정보 삭제
            List<BoardsLike> boardsLikes = boardsLikeRepository.findAllByBoardId(board.getBoardId());
            boardsLikeRepository.deleteAll(boardsLikes);

            // 해당 게시글의 댓글 정보 삭제 -> Boards필드의 boardId와 같은 값의 댓글들 정보를 조회
            List<Comment> comments = commentRepository.findAllByBoards_BoardId(board.getBoardId());
            for(Comment comment : comments) {
                // 관련된 각 댓글에 달린 좋아요 정보 삭제
                List<CommentLike> commentLikes = comment.getLikes();
                // 해당 댓글에 좋아요가 달려있는지 확인
                if(commentLikes != null && !commentLikes.isEmpty()) {
                    commentLikeRepository.deleteAll(commentLikes);
                }
            }
            commentRepository.deleteAll(comments);

            boardsRepository.delete(board);
        }

        // 유저가 작성한 댓글 정보 삭제
        List<Comment> userComments = commentRepository.findAllByUser_UserId(userId);
        for(Comment comment : userComments) {
            List<CommentLike> commentLikes = comment.getLikes();
            // 해당 댓글에 좋아요가 달려있는지 확인
            if (commentLikes != null && !commentLikes.isEmpty()) {
                commentLikeRepository.deleteAll(commentLikes);
            }
        }
        commentRepository.deleteAll(userComments);

        // 유저가 직접 누른 모든 CommentLike 데이터 삭제
        List<CommentLike> userCommentLikes = commentLikeRepository.findAllByUser_UserId(userId);
        commentLikeRepository.deleteAll(userCommentLikes);

        // 유저가 좋아요를 누른 모든 BoardsLike 데이터 삭제
        List<BoardsLike> userLikes = boardsLikeRepository.findAllByUserId(userId);
        boardsLikeRepository.deleteAll(userLikes);

        // 해당 유저가 포함된 모든 친구 관계 삭제 (fromUser, toUser)
        List<Friend> userFriendsAsFrom = friendRepository.findAllByFromUser(userId);
        List<Friend> userFriendsAsTo = friendRepository.findAllByToUser(userId);
        friendRepository.deleteAll(userFriendsAsFrom);
        friendRepository.deleteAll(userFriendsAsTo);

        // 해당 유저 삭제 상태 업데이트
        newUser.setDeleteStatus(true);
    }
}
