package com.allra.allrabackendtask.market.domain.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 상품 조회 검색 요청 DTO
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchReq {

    private Long categoryId;        // 카테고리 ID로 검색
    private String name;            // 상품명으로 검색
    private Integer minPrice;       // 최소 가격
    private Integer maxPrice;       // 최대 가격
    private Boolean isAvailable;    // 품절 여부
}
