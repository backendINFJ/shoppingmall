package com.sparta.shoppingmall.comment.controller;

import com.sparta.shoppingmall.base.dto.CommonResponse;
import com.sparta.shoppingmall.comment.dto.CommentRequest;
import com.sparta.shoppingmall.comment.dto.CommentResponse;
import com.sparta.shoppingmall.comment.service.CommentService;
import com.sparta.shoppingmall.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.shoppingmall.util.ControllerUtil.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/{productId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     */
    @PostMapping
    public ResponseEntity<CommonResponse> createComment(
            @PathVariable Long productId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "댓글 생성 실패");
        }
        try{
            CommentResponse response = commentService.createComment(request, productId, userDetails.getUser());
            return getResponseEntity(response, "댓글 생성 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 해당 상품의 전체 댓글 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse> getComments(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try{
            List<CommentResponse> response = commentService.getComments(productId);
            return getResponseEntity(response, "댓글 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(
            @PathVariable Long productId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return getFieldErrorResponseEntity(bindingResult, "댓글 수정 실패");
        }
        try{
            CommentResponse response = commentService.updateComments(request, productId, commentId, userDetails.getUser());
            return getResponseEntity(response, "댓글 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(
            @PathVariable Long productId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try{
            Long response = commentService.deleteComment(productId, commentId, userDetails.getUser());
            return getResponseEntity(response, "댓글 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}
