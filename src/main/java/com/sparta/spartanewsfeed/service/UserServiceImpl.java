package com.sparta.spartanewsfeed.service;

import com.sparta.spartanewsfeed.dto.*;
import com.sparta.spartanewsfeed.entity.Boards;
import com.sparta.spartanewsfeed.entity.BoardsLike;
import com.sparta.spartanewsfeed.entity.Friend;
import com.sparta.spartanewsfeed.entity.User;
import com.sparta.spartanewsfeed.exception.NotFoundException;
import com.sparta.spartanewsfeed.exception.PasswordErrorException;
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
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BoardsRepository boardsRepository;
    private final BoardsLikeRepository boardsLikeRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Transactional
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        // email 중복 체크
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 이메일입니다.");
        }

        String password = passwordEncoder.encode(userRequestDto.getPassword());
        User user = new User(userRequestDto);
        user.setPassword(password);
        User saveUser = userRepository.save(user);
        return new UserResponseDto(saveUser);
    }

    @Transactional
    @Override
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse res) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("등록된 사용자가 없습니다.")
        );
        if(user.getDeleteStatus().equals(true)) {
            throw new NotFoundException("탈퇴한 유저입니다.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordErrorException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUserId(), user.getName());

        jwtUtil.addJwtToCookie(token, res); // 쿠키를 HTTP 응답에 추가
        return token;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto get(Long userId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));
        return new UserResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto modify(Long userId, UserModifyRequestDto userModifyRequestDto, User user) {

        if(!userId.equals(user.getUserId())) {
            throw new SecurityException("해당 유저에 대한 엑세스 권한이 없습니다!");
        }

        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));

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
                throw new PasswordErrorException("비밀번호가 일치하지 않습니다.");
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
    @Override
    public void delete(Long userId, UserDeleteRequestDto userDeleteRequestDto, User user) {

        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));

        if(newUser.getDeleteStatus().equals(true)) {
            throw new NotFoundException("이미 탈퇴한 유저입니다!");
        }

        if(!userId.equals(user.getUserId())) {
            throw new SecurityException("해당 유저에 대한 엑세스 권한이 없습니다!");
        }

        String password = userDeleteRequestDto.getPassword();
        if(!passwordEncoder.matches(password, newUser.getPassword())) {
            throw new PasswordErrorException("비밀번호가 일치하지 않습니다.");
        }

        // 관련 board 데이터 삭제
        List<Boards> userBoards = boardsRepository.findAllByUserId(userId);
        for(Boards board : userBoards) {
            boardsRepository.delete(board);

            // 해당 게시글의 좋아요 정보 삭제
            List<BoardsLike> boardsLikes = boardsLikeRepository.findAllByBoardId(board.getBoardId());
            boardsLikeRepository.deleteAll(boardsLikes);
        }

        // 유저가 좋아요를 누른 모든 BoardsLike 데이터 삭제
        List<BoardsLike> userLikes = boardsLikeRepository.findAllByUserId(userId);
        boardsLikeRepository.deleteAll(userLikes);

        // 해당 유저가 포함된 모든 친구 관계 삭제 (fromUser, toUser)
        List<Friend> userFriendsAsFrom = friendRepository.findAllByFromUser(user);
        List<Friend> userFriendsAsTo = friendRepository.findAllByToUser(user);
        friendRepository.deleteAll(userFriendsAsFrom);
        friendRepository.deleteAll(userFriendsAsTo);

        // 해당 유저 삭제 상태 업데이트
        newUser.setDeleteStatus(true);
    }
}
