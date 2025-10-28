package com.allra.allrabackendtask.market.domain.product.entity;

import com.allra.allrabackendtask.market.common.CommonEntity;
import com.allra.allrabackendtask.market.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB03PRDCT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    public boolean isOutOfStock() {
        return this.stockQuantity <= 0;
    }

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
        if (this.stockQuantity == 0) {
            this.isAvailable = false;
        }
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
        if (this.stockQuantity > 0) {
            this.isAvailable = true;
        }
    }
}