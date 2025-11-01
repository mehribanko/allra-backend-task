package com.allra.allrabackendtask.market.domain.order;


import com.allra.allrabackendtask.market.domain.cart.entity.Cart;
import com.allra.allrabackendtask.market.domain.cart.entity.CartItem;
import com.allra.allrabackendtask.market.domain.category.entity.Category;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.user.entity.User;

public class TestDataFactory {

    public static User createUser() {
        return User.builder()
                .email("test@test.com")
                .name("Test User")
                .phone("010-1234-5678")
                .address("Seoul, Test Address")
                .build();
    }

    public static Category createCategory() {
        return Category.builder()
                .name("테스트 카테고리")
                .description("테스트용 카테고리")
                .build();
    }

    public static Product createProduct(Category category, int stockQuantity) {
        return Product.builder()
                .name("테스트 상품")
                .description("테스트용 상품")
                .price(10000)
                .stockQuantity(stockQuantity)
                .isAvailable(true)
                .category(category)
                .build();
    }

    public static Cart createCart(User user) {
        return Cart.builder()
                .user(user)
                .build();
    }

    public static CartItem createCartItem(Cart cart, Product product, int quantity) {
        return CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();
    }
}
