package com.allra.allrabackendtask.market.domain.product.service;

import com.allra.allrabackendtask.market.domain.product.dto.ProductSearchReq;
import com.allra.allrabackendtask.market.domain.product.dto.ProductSearchRes;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 검색 서비스 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 검색 (필터링 + 페이징)
     */
    public Page<ProductSearchRes> searchProducts(ProductSearchReq searchRequest, Pageable pageable) {
        log.info("=============== ProductService =================");
        log.info("상품 조회 중 [필터] --->  {}", searchRequest);

        Page<Product> productPage = productRepository.searchProducts(searchRequest, pageable);

        return productPage.map(ProductSearchRes::from);
    }

    /**
     * 상품 상세 조회
     */
    public ProductSearchRes getProductById(Long productId) {
        log.info("=============== ProductService =================");
        log.info("상품 개별 조회 중 [상품 id] --->  {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productId));

        return ProductSearchRes.from(product);
    }
}
