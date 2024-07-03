package com.sparta.shoppingmall.user.service;


import com.sparta.shoppingmall.jwt.RefreshTokenService;
import com.sparta.shoppingmall.like.entity.ContentType;
import com.sparta.shoppingmall.like.entity.LikeStatus;
import com.sparta.shoppingmall.like.repository.LikesRepository;
import com.sparta.shoppingmall.user.dto.*;
import com.sparta.shoppingmall.user.entity.User;
import com.sparta.shoppingmall.user.entity.UserStatus;
import com.sparta.shoppingmall.user.entity.UserType;
import com.sparta.shoppingmall.user.exception.UserException;
import com.sparta.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final LikesRepository likesRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public SignupResponseDTO createUser(SignupRequestDTO request) {
        //아이디 중복 검사
        validateUsername(request.getUsername());

        //비밀번호 암호화
        String password = passwordEncoder.encode(request.getPassword());
        User user = new User(request, password, request.getUserType(), UserStatus.JOIN, LocalDateTime.now());

        userRepository.save(user);

        return new SignupResponseDTO(user);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public Long withdrawUser(WithdrawRequestDTO request, User user) {
        //회원 상태 확인
        checkUserStatus(user.getUserStatus());

        //비밀번호 일치 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

        //회원 상태 변경
        user.changeStatus(UserStatus.WITHDRAW);
        userRepository.save(user);

        return user.getId();
    }

    /**
     * 로그아웃
     */
    @Transactional
    public Long logout(Long userId) {
        //사용자 조회
        User user = findById(userId);

        refreshTokenService.deleteToken(user.getUsername());

        return user.getId();
    }


    /**
     * 회원 조회 (유저 아이디)
     */
    public EditProfileResponseDTO inquiryUser(Long userId, User user) {
        if(!Objects.equals(user.getId(), userId)) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
        return new EditProfileResponseDTO(user);
    }


    /**
     * 회원 프로필 수정
     */
    @Transactional // 변경할 필드만 수정하고 바꾸지 않은 필드는 기존 데이터를 유지하는 메서드
    public UserResponseDTO editProfile(Long userId, EditProfileRequestDTO request, User user) {
        if(!Objects.equals(userId, user.getId())){
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
        user.editProfile(request.getName(), request.getEmail(), request.getAddress());
        userRepository.save(user);

        return new UserResponseDTO(user);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public UserResponseDTO editPassword(EditPasswordRequestDTO request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new UserException("새로운 비밀번호와 기존 비밀번호가 동일합니다.");
        }

        // 새로운 비밀번호가 최근 비밀번호들과 일치하는지 확인 - > 유효성 검사
        if (passwordEncoder.matches(request.getNewPassword(), user.getRecentPassword())
                || passwordEncoder.matches(request.getNewPassword(), user.getRecentPassword2())
                || passwordEncoder.matches(request.getNewPassword(), user.getRecentPassword3())) {
            throw new UserException("새로운 비밀번호는 최근 사용한 비밀번호와 다르게 설정해야 합니다.");
        }

        String editPassword = passwordEncoder.encode(request.getNewPassword());

        // 최근3개 비밀번호 저장
        user.changePassword(editPassword);
        userRepository.save(user);

        return new UserResponseDTO(user);
    }


    /**
     * 회원 전체 조회 - 관리자
     */
    public List<AdminUserResponse> getUserList() {
        List<User> userList = userRepository.findAll();
        List<AdminUserResponse> response = new ArrayList<>();
        for (User findUser : userList) {
            response.add(new AdminUserResponse(findUser));
        }
        return response;
    }

    /**
     * 회원 정보 수정 - 관리자
     */
    @Transactional
    public AdminUserResponse updateUser(AdminUpdateUserRequest request, Long userId) {
        User findUser = findById(userId);

        findUser.adminUpdateUser(request);

        return new AdminUserResponse(findUser);
    }

    /**
     * 회원 상태 확인
     */
    private void checkUserStatus(UserStatus userStatus) {
        if (userStatus.equals(UserStatus.WITHDRAW)) {
            throw new UserException("이미 탈퇴한 회원입니다.");
        }
    }

    /**
     * username 유효성 검사
     */
    private void validateUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            throw new UserException("중복된 username 입니다.");
        }
    }

    /**
     * PK로 user 찾기
     */
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다.")
        );
    }

    /**
     * 프로필 조회시 응답필드 게시글 & 댓글 필드 추가
     */

    public UserProfileDTO getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );
        UserResponseDTO response = new UserResponseDTO(user);
        long likedPostsCount = likesRepository.countByUserAndStatusAndContentType(user, LikeStatus.LIKED, ContentType.PRODUCT);
        long likedCommentsCount = likesRepository.countByUserAndStatusAndContentType(user, LikeStatus.LIKED, ContentType.COMMENT);
        return new UserProfileDTO(response,likedPostsCount,likedCommentsCount);
    } // 3
}
