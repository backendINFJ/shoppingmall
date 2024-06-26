package com.sparta.shoppingmall.product.controller;

import com.sparta.shoppingmall.base.dto.CommonResponse;
import com.sparta.shoppingmall.product.dto.ProductRequest;
import com.sparta.shoppingmall.product.dto.ProductResponse;
import com.sparta.shoppingmall.product.service.ProductService;
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
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품등록
     */
    @PostMapping
    public ResponseEntity<CommonResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return getFieldErrorResponseEntity(bindingResult, "상품 추가 실패");
        }
        try{
            ProductResponse response = productService.createProduct(productRequest, userDetails.getUser().getId());
            return getResponseEntity(response, "상품 등록 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 상품조회(전체) get api/products
     */
    @GetMapping
    public ResponseEntity<CommonResponse> getProducts(
            @RequestParam int page
    ) {
        try{
            List<ProductResponse> response = productService.getProducts(page);
            return getResponseEntity(response, "상품 목록 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }


    /**
     * 상품조회(단일)get api/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse> getProduct(
            @PathVariable Long productId
    ){
        try{
            ProductResponse response = productService.getProduct(productId);
            return getResponseEntity(response, "상품 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }


    /**
     * 상품수정 api/products/{productId}
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<CommonResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try{
            ProductResponse response = productService.updateProduct(productId, productRequest, userDetails.getUser());
            return getResponseEntity(response, "상품 수정 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    /**
     * 상품삭제 delete  api/products
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try{
            Long response = productService.deleteProduct(productId, userDetails.getUser());
            return getResponseEntity(response, "상품 삭제 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}

