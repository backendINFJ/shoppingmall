package com.sparta.shoppingmall.product.service;

import com.sparta.shoppingmall.exception.UserMismatchException;
import com.sparta.shoppingmall.product.dto.ProductRequest;
import com.sparta.shoppingmall.product.dto.ProductResponse;
import com.sparta.shoppingmall.product.entity.Product;
import com.sparta.shoppingmall.product.entity.ProductStatus;
import com.sparta.shoppingmall.product.repository.ProductRepository;
import com.sparta.shoppingmall.user.entity.User;
import com.sparta.shoppingmall.user.entity.UserType;
import com.sparta.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 상품등록
     */
    public ProductResponse createProduct(ProductRequest productRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        Product product = Product.builder()
                .user(user)
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .status(ProductStatus.ONSALE)
                .build();

        productRepository.save(product);

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .user(product.getUser())
                .build();
    }


    /**
     * 상품조회(전체) get api/products -> 5개씩 / 정렬 = 최신순, 추천, 판매중 상품
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(int page) {
        Pageable pageable = PageRequest.of(page-1, 5);

        List<ProductStatus> condi = new ArrayList<>();
        condi.add(ProductStatus.ONSALE);
        condi.add(ProductStatus.RECOMMAND);

        Page<Product> products = productRepository.findAllByStatusInOrderByCreateAtDescStatusAsc(condi, pageable);

        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .status(product.getStatus())
                    .user(product.getUser())
                    .build();
            productResponses.add(productResponse);
        }

        return productResponses;
    }


    /**
     * 상품조회(단일)get api/products/{productId}
     */
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .likeCounts(product.getLikeCount())
                .user(product.getUser())
                .build();
    }

    /**
     * 상품수정 api/products/{productId}
     */
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest, User user) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NullPointerException("Product with id " + productId + " not found")
        );

        if(user.getUserType() != UserType.ADMIN){//유저일때가 아니라 관리자가 아닐때 로 적는게 맞음.
            if(!Objects.equals(user.getId(), product.getUser().getId())){
                throw new UserMismatchException("권한이 없는 사용자입니다");
            }
        }

        product.update(
                productRequest.getName(),
                productRequest.getPrice()
        );
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .status(product.getStatus())
                .user(product.getUser())
                .build();
    }


    /**
     * 상품삭제 delete  api/products
     */
    @Transactional
    public Long deleteProduct(Long productId, User user) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new IllegalArgumentException("해당 상품은 존재하지 않습니다.")
        );

        if(user.getUserType() != UserType.ADMIN){//유저일때가 아니라 관리자가 아닐때 로 적는게 맞음.
            if(!Objects.equals(user.getId(), product.getUser().getId())){
                throw new UserMismatchException("권한이 없는 사용자입니다");
            }
        }

        productRepository.delete(product);

        return product.getId();
    }

    /**
     * 상품 조회
     */
    public Product findByProductId(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
    }

}