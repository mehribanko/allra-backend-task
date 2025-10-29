package com.allra.allrabackendtask.market.domain.payment.entity;


import com.allra.allrabackendtask.market.common.CommonEntity;
import com.allra.allrabackendtask.market.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB08PAYMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(length = 100)
    private String transactionId;

    @Column(length = 200)
    private String message;


    public void completePayment(String transactionId) {
        this.status = PaymentStatus.SUCCESS;
        this.transactionId = transactionId;
        this.message = "Payment processed successfully";
    }

    public void failPayment(String message) {
        this.status = PaymentStatus.FAILED;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.status == PaymentStatus.SUCCESS;
    }
}
