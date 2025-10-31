package com.allra.allrabackendtask.market.domain.order.dto;

import com.allra.allrabackendtask.market.domain.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRes {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;

    public static OrderItemRes from(OrderItem orderItem) {
        return OrderItemRes.builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}