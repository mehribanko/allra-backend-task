package com.allra.allrabackendtask.market.domain.order.entity;


import com.allra.allrabackendtask.market.common.CommonEntity;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB07ORDERITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItem extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    public int getTotalPrice() {
        return this.price * this.quantity;
    }
}