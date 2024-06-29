package com.sparta.shoppingmall.product.repository;

import com.sparta.shoppingmall.product.entity.Product;
import com.sparta.shoppingmall.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findAllByStatusInOrderByCreateAtDescStatusAsc(List<ProductStatus> condi, Pageable pageable);
}
