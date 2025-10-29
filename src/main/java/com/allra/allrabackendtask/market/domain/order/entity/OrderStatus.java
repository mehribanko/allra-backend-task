package com.allra.allrabackendtask.market.domain.order.entity;

public enum OrderStatus {
    PENDING("주문 생성"),
    PAYMENT_PENDING("결제 대기"),
    PAYMENT_COMPLETED("결제 완료"),
    CANCELLED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}