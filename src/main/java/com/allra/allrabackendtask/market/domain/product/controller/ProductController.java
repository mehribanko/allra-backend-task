package com.allra.allrabackendtask.market.domain.product.controller;

import com.allra.allrabackendtask.market.domain.product.dto.ProductSearchReq;
import com.allra.allrabackendtask.market.domain.product.dto.ProductSearchRes;
import com.allra.allrabackendtask.market.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 검색 API
 * v1
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 검색 API
     * GET /api/v1/products?categoryId=1&name=iPhone&minPrice=1000000&maxPrice=2000000&isAvailable=true&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<ProductSearchRes>> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Boolean isAvailable,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("상품 조회 필터 - categoryId: {}, name: {}, minPrice: {}, maxPrice: {}, isAvailable: {}",
                categoryId, name, minPrice, maxPrice, isAvailable);

        ProductSearchReq searchRequest = ProductSearchReq.builder()
                .categoryId(categoryId)
                .name(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .isAvailable(isAvailable)
                .build();

        Page<ProductSearchRes> products = productService.searchProducts(searchRequest, pageable);

        return ResponseEntity.ok(products);
    }

    /**
     * 상품 상세 조회 API
     * GET /api/v1/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductSearchRes> getProduct(@PathVariable Long productId) {
        log.info("상품 개별 조회 - productId: {}", productId);

        ProductSearchRes product = productService.getProductById(productId);

        return ResponseEntity.ok(product);
    }
}