package com.sparta.shoppingmall.cart.controller;

import com.sparta.shoppingmall.base.dto.CommonResponse;
import com.sparta.shoppingmall.cart.dto.CartProductRequest;
import com.sparta.shoppingmall.cart.dto.CartProductResponse;
import com.sparta.shoppingmall.cart.dto.CartResponse;
import com.sparta.shoppingmall.cart.service.CartService;
import com.sparta.shoppingmall.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.sparta.shoppingmall.util.ControllerUtil.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니에 상품 단건 담기
     */
    @PostMapping("/products")
    public ResponseEntity<CommonResponse> creatCartProduct(
            @Valid @RequestBody CartProductRequest cartProductRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "장바구니에 상품 추가 실패");
        }
        try{
            CartProductResponse response = cartService.addCartProduct(cartProductRequest, userDetails.getUser());
            return getResponseEntity(response, "장바구니에 상품 추가 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 장바구니에 상품 리스트 조회
     */
    @GetMapping("/products")
    public ResponseEntity<CommonResponse> getCartProducts(
            @RequestParam int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            CartResponse response = cartService.getCartProducts(page, userDetails.getUser());
            return getResponseEntity(response, "장바구니 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 장바구니에 상품 단건 삭제
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CommonResponse> deleteCartProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            Long response = cartService.deleteCartProduct(productId, userDetails.getUser());
            return getResponseEntity(response, "장바구니에 상품 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

}
