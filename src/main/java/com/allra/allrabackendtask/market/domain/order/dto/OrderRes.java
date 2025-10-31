package com.allra.allrabackendtask.market.domain.order.dto;
import com.allra.allrabackendtask.market.domain.order.entity.Order;
import com.allra.allrabackendtask.market.domain.order.entity.OrderStatus;
import com.allra.allrabackendtask.market.domain.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRes {

    private Long orderId;
    private Long userId;
    private String userName;
    private List<OrderItemRes> orderItems;
    private Integer totalAmount;
    private String deliveryAddress;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String paymentTransactionId;
    private LocalDateTime createdAt;

    public static OrderRes from(Order order) {
        List<OrderItemRes> items = order.getOrderItems().stream()
                .map(OrderItemRes::from)
                .collect(Collectors.toList());

        return OrderRes.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .orderItems(items)
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .orderStatus(order.getStatus())
                .paymentStatus(order.getPayment() != null ? order.getPayment().getStatus() : null)
                .paymentTransactionId(order.getPayment() != null ? order.getPayment().getTransactionId() : null)
                .createdAt(order.getCreatedAt())
                .build();
    }
}