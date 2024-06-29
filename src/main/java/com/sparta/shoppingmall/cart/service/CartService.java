package com.sparta.shoppingmall.cart.service;

import com.sparta.shoppingmall.cart.dto.CartProductRequest;
import com.sparta.shoppingmall.cart.dto.CartProductResponse;
import com.sparta.shoppingmall.cart.dto.CartResponse;
import com.sparta.shoppingmall.cart.entity.Cart;
import com.sparta.shoppingmall.cart.entity.CartProduct;
import com.sparta.shoppingmall.cart.repository.CartProductRepository;
import com.sparta.shoppingmall.cart.repository.CartRepository;
import com.sparta.shoppingmall.product.entity.Product;
import com.sparta.shoppingmall.product.service.ProductService;
import com.sparta.shoppingmall.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.shoppingmall.cart.entity.CartProduct.createCartProduct;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductService productService;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    /**
     * 장바구니에 상품 단건 담기
     */
    @Transactional
    public CartProductResponse addCartProduct(CartProductRequest request, User user) {
        Product product = productService.findByProductId(request.getProductId());
        //상품 상태 체크
        if(!product.checkProductStatus()){
            throw new IllegalArgumentException("판매 중인 상품이 아닙니다.");
        }

        //장바구니가 없으면 생성
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user);
        }

        //장바구니 안에 상품 중복 확인
        cart.getCartProducts().forEach(cartProduct -> cartProduct.verifyCartProduct(product.getId()));

        CartProduct cartProduct = createCartProduct(cart, product);
        cartRepository.save(cart);

        return new CartProductResponse(cartProduct.getId(), cart, product);
    }

    /**
     * 장바구니에 상품 리스트 조회
     */
    @Transactional
    public CartResponse getCartProducts(int page, User user) {
        Pageable pageable = PageRequest.of(page-1, 5);
        Cart cart = user.getCart();

        //상품 상태 확인 후 삭제
        List<CartProduct> cartProductList = cart.getCartProducts();
        for (CartProduct cartProduct : cartProductList) {
            if(!cartProduct.getProduct().checkProductStatus()){
                cartProductRepository.delete(cartProduct);
            }
        }

        Page<CartProduct> cartProducts = cartProductRepository.findAllByCartId(cart.getId(), pageable);

        return CartResponse.builder()
                .id(cart.getId())
                .cartProducts(cartProducts)
                .build();
    }

    /**
     * 장바구니에 상품 단건 삭제
     */
    @Transactional
    public Long deleteCartProduct(Long productId, User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("장바구니가 존재하지 않습니다. 상품을 담은 후 시도해주세요")
        );

        CartProduct cartProduct = cartProductRepository.findByProductId(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다.")
        );
        cart.removeCartProduct(cartProduct);

        return cartProduct.getId();
    }
}
