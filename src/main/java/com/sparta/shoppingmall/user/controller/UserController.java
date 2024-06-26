package com.sparta.shoppingmall.user.controller;

import com.sparta.shoppingmall.base.dto.CommonResponse;
import com.sparta.shoppingmall.security.UserDetailsImpl;
import com.sparta.shoppingmall.user.dto.*;
import com.sparta.shoppingmall.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.shoppingmall.util.ControllerUtil.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> createUser(
            @Valid @RequestBody SignupRequestDTO requestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원가입 실패");
        }
        try{
            log.info("test");
            SignupResponseDTO response = userService.createUser(requestDTO);
            return getResponseEntity(response, "회원가입 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 회원 탈퇴
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<CommonResponse> withdrawUser(
            @Valid @RequestBody WithdrawRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "회원탈퇴 실패");
        }
        try {
            Long response = userService.withdrawUser(requestDTO, userDetails.getUser());
            return getResponseEntity(response, "회원탈퇴 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }

    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponse> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = userService.logout(userDetails.getUser().getId());
            return getResponseEntity(response, "로그아웃 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 로그인한 사용자 프로필 조회
     */
    @GetMapping("/{userId}/profiles")
    public ResponseEntity<CommonResponse> userProfile(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            EditProfileResponseDTO response = userService.inquiryUser(userId, userDetails.getUser());
            return getResponseEntity(response, "프로필 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 로그인한 사용자 프로필 수정
     */
    @PatchMapping("/{userId}/profiles")
    public ResponseEntity<CommonResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody EditProfileRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "프로필 수정 실패");
        }
        try{
            UserResponseDTO response = userService.editProfile(userId, request, userDetails.getUser());
            return getResponseEntity(response, "프로필 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/update-password")
    public ResponseEntity<CommonResponse> updatePassword(
            @Valid @RequestBody EditPasswordRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "비밀번호 변경 실패");
        }
        try{
            UserResponseDTO response = userService.editPassword(request, userDetails.getUser());
            return getResponseEntity(response, "비밀번호 변경 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 회원 전체조회 - 관리자
     */
    @Secured("ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<CommonResponse> getUserList(){
        try{
            List<AdminUserResponse> response = userService.getUserList();
            return getResponseEntity(response, "회원 전체 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 회원 정보 수정 - 관리자
     */
    @Secured("ADMIN")
    @PutMapping("/{userId}/admin")
    public ResponseEntity<CommonResponse> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {
        try{
            AdminUserResponse response = userService.updateUser(request, userId);
            return getResponseEntity(response, "회원 전체 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}
