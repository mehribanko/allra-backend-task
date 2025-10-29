package com.allra.allrabackendtask.market.domain.cart.entity;


import com.allra.allrabackendtask.market.common.CommonEntity;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB05CARTITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItem extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @Setter
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
        if (this.quantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }
    }

    public int getTotalPrice() {
        return this.product.getPrice() * this.quantity;
    }

    public boolean isProductAvailable() {
        return this.product.getIsAvailable() && !this.product.isOutOfStock();
    }
}
