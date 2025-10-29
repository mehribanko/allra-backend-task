package com.allra.allrabackendtask.market.domain.product.dto;

import com.allra.allrabackendtask.market.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 조회 검색 답변 DTO
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchRes {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer stockQuantity;
    private Boolean isAvailable;
    private String categoryName;
    private Long categoryId;

    public static ProductSearchRes from(Product product) {
        return ProductSearchRes.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.getIsAvailable())
                .categoryName(product.getCategory().getName())
                .categoryId(product.getCategory().getId())
                .build();
    }
}