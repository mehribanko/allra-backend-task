package com.allra.allrabackendtask.market.domain.cart.dto;

import com.allra.allrabackendtask.market.domain.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRes {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;
    private Boolean isAvailable;
    private Integer stockQuantity;
    private String categoryName;

    public static CartItemRes from(CartItem cartItem) {
        return CartItemRes.builder()
                .cartItemId(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .price(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .isAvailable(cartItem.isProductAvailable())  // 품절 여부 체크
                .stockQuantity(cartItem.getProduct().getStockQuantity())
                .categoryName(cartItem.getProduct().getCategory().getName())
                .build();
    }
}