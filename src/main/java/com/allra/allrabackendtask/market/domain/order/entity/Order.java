package com.allra.allrabackendtask.market.domain.order.entity;


import com.allra.allrabackendtask.market.common.CommonEntity;
import com.allra.allrabackendtask.market.domain.payment.entity.Payment;
import com.allra.allrabackendtask.market.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB06ORDER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(length = 200)
    private String deliveryAddress;

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void completePayment() {
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    public void cancelOrder() {
        if (this.status == OrderStatus.PAYMENT_COMPLETED) {
            throw new IllegalStateException("결제 완료된 주문은 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public boolean isPaid() {
        return this.status == OrderStatus.PAYMENT_COMPLETED;
    }
}