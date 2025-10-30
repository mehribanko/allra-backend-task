package com.allra.allrabackendtask.market.domain.cart.dto;

import com.allra.allrabackendtask.market.domain.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRes {

    private Long cartId;
    private Long userId;
    private String userName;
    private List<CartItemRes> items;
    private Integer totalPrice;
    private Integer totalItems;

    public static CartRes from(Cart cart) {

        List<CartItemRes> items = cart.getCartItems() != null
                ? cart.getCartItems().stream()
                .map(CartItemRes::from)
                .collect(Collectors.toList())
                : Collections.emptyList();

        Integer totalPrice = cart.getCartItems() != null && !cart.getCartItems().isEmpty()
                ? cart.getTotalPrice()
                : 0;

        return CartRes.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .userName(cart.getUser().getName())
                .items(items)
                .totalPrice(totalPrice)
                .totalItems(items.size())
                .build();
    }
}