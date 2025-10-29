package com.allra.allrabackendtask.market.domain.cart.entity;

import com.allra.allrabackendtask.market.common.CommonEntity;
import com.allra.allrabackendtask.market.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB04CART")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Cart extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeCartItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    public void clearCart() {
        this.cartItems.clear();
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }
}