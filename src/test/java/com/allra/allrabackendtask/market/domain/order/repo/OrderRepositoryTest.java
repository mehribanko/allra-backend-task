package com.allra.allrabackendtask.market.domain.order.repo;


import com.allra.allrabackendtask.market.common.config.QueryDSLConfig;
import com.allra.allrabackendtask.market.domain.category.entity.Category;
import com.allra.allrabackendtask.market.domain.category.repo.CategoryRepository;
import com.allra.allrabackendtask.market.domain.order.TestDataFactory;
import com.allra.allrabackendtask.market.domain.order.entity.Order;
import com.allra.allrabackendtask.market.domain.order.entity.OrderItem;
import com.allra.allrabackendtask.market.domain.order.entity.OrderStatus;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.product.repo.ProductRepository;
import com.allra.allrabackendtask.market.domain.user.entity.User;
import com.allra.allrabackendtask.market.domain.user.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.createUser();
        userRepository.save(testUser);

        Category category = TestDataFactory.createCategory();
        categoryRepository.save(category);

        testProduct = TestDataFactory.createProduct(category, 100);
        productRepository.save(testProduct);
    }

    @Test
    @DisplayName("주문 저장")
    void saveOrder() {
        Order order = Order.builder()
                .user(testUser)
                .totalAmount(10000)
                .deliveryAddress("Test Address")
                .status(OrderStatus.PENDING)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .product(testProduct)
                .quantity(1)
                .price(10000)
                .build();

        order.addOrderItem(orderItem);
        Order saved = orderRepository.save(order);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(saved.getOrderItems()).hasSize(1);
    }

    @Test
    @DisplayName("사용자별 주문 조회")
    void findByUserId() {
        for (int i = 0; i < 3; i++) {
            Order order = Order.builder()
                    .user(testUser)
                    .totalAmount(10000 * (i + 1))
                    .deliveryAddress("Test Address " + i)
                    .status(OrderStatus.PENDING)
                    .build();
            orderRepository.save(order);
        }

        List<Order> orders = orderRepository.findByUserId(testUser.getId());
        //
        assertThat(orders).hasSize(3);
        assertThat(orders).allMatch(order -> order.getUser().getId().equals(testUser.getId()));
    }

    @Test
    @DisplayName("주문 상세 조회 (OrderItem 포함)")
    void findByIdWithItems() {
        Order order = Order.builder()
                .user(testUser)
                .totalAmount(20000)
                .deliveryAddress("Test Address")
                .status(OrderStatus.PENDING)
                .build();

        OrderItem item1 = OrderItem.builder()
                .product(testProduct)
                .quantity(1)
                .price(10000)
                .build();

        OrderItem item2 = OrderItem.builder()
                .product(testProduct)
                .quantity(1)
                .price(10000)
                .build();

        order.addOrderItem(item1);
        order.addOrderItem(item2);

        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findByIdWithItems(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getOrderItems()).hasSize(2);
    }

    @Test
    @DisplayName("사용자 및 주문 상태로 조회")
    void findByUserIdAndStatus() {
        Order order1 = Order.builder()
                .user(testUser)
                .totalAmount(10000)
                .deliveryAddress("Address 1")
                .status(OrderStatus.PAYMENT_COMPLETED)
                .build();

        Order order2 = Order.builder()
                .user(testUser)
                .totalAmount(20000)
                .deliveryAddress("Address 2")
                .status(OrderStatus.CANCELLED)
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> completedOrders = orderRepository.findByUserIdAndStatus(
                testUser.getId(),
                OrderStatus.PAYMENT_COMPLETED
        );


        assertThat(completedOrders).hasSize(1);
        assertThat(completedOrders.get(0).getStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
    }
}
