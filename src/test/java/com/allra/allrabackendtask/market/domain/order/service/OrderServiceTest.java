package com.allra.allrabackendtask.market.domain.order.service;

import com.allra.allrabackendtask.market.domain.cart.entity.Cart;
import com.allra.allrabackendtask.market.domain.cart.entity.CartItem;
import com.allra.allrabackendtask.market.domain.cart.repo.CartItemRepository;
import com.allra.allrabackendtask.market.domain.cart.repo.CartRepository;
import com.allra.allrabackendtask.market.domain.category.entity.Category;
import com.allra.allrabackendtask.market.domain.category.repo.CategoryRepository;
import com.allra.allrabackendtask.market.domain.order.TestDataFactory;
import com.allra.allrabackendtask.market.domain.order.dto.OrderCreateReq;
import com.allra.allrabackendtask.market.domain.order.dto.OrderRes;
import com.allra.allrabackendtask.market.domain.order.entity.OrderStatus;
import com.allra.allrabackendtask.market.domain.order.repo.OrderRepository;
import com.allra.allrabackendtask.market.domain.payment.dto.PaymentRes;
import com.allra.allrabackendtask.market.domain.payment.entity.PaymentStatus;
import com.allra.allrabackendtask.market.domain.payment.repo.PaymentRepository;
import com.allra.allrabackendtask.market.domain.payment.service.PaymentClient;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.product.repo.ProductRepository;
import com.allra.allrabackendtask.market.domain.user.entity.User;
import com.allra.allrabackendtask.market.domain.user.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @MockitoBean
    private PaymentClient paymentClient;

    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성
        testUser = TestDataFactory.createUser();
        userRepository.save(testUser);

        Category category = TestDataFactory.createCategory();
        categoryRepository.save(category);

        testProduct = TestDataFactory.createProduct(category, 100);
        productRepository.save(testProduct);

        testCart = TestDataFactory.createCart(testUser);
        cartRepository.save(testCart);
    }

    @Test
    @DisplayName("주문 생성 성공 // 결제 성공")
    void createOrder_Success_WithPaymentSuccess() {
        // 장바구니에 상품 추가
        CartItem cartItem = TestDataFactory.createCartItem(testCart, testProduct, 2);
        testCart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        OrderCreateReq request = new OrderCreateReq(
                testUser.getId(),
                "Test delivery address"
        );

        // 결제 성공
        PaymentRes paymentResponse = new PaymentRes(
                "SUCCESS",
                "txn_test_123456",
                "Payment processed successfully"
        );
        given(paymentClient.processPayment(any())).willReturn(paymentResponse);

        // 주문 생성
        OrderRes result = orderService.createOrder(request);

        // check check
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(result.getPaymentTransactionId()).isEqualTo("txn_test_123456");
        assertThat(result.getTotalAmount()).isEqualTo(20000);
        assertThat(result.getOrderItems()).hasSize(1);

        // 재고 확인
        Product updatedProduct = productRepository.findById(testProduct.getId()).get();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(98);

        // 장바구니 비워졌는지 확인
        Cart updatedCart = cartRepository.findById(testCart.getId()).get();
        assertThat(updatedCart.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("주문 생성 성공 // 결제 실패 (재고 복구)")
    void createOrder_Success_WithPaymentFailure() {
        // 장바구니에 상품 추가
        CartItem cartItem = TestDataFactory.createCartItem(testCart, testProduct, 3);
        testCart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        OrderCreateReq request = new OrderCreateReq(
                testUser.getId(),
                "Test delivery"
        );

        // 결제 실패 응답
        PaymentRes paymentResponse = new PaymentRes(
                "FAILED",
                null,
                "Payment failed"
        );
        given(paymentClient.processPayment(any())).willReturn(paymentResponse);

        int originalStock = testProduct.getStockQuantity();

        // 주문 생성
        OrderRes result = orderService.createOrder(request);

        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(result.getPaymentTransactionId()).isNull();

        // 재고 복구 확인
        Product updatedProduct = productRepository.findById(testProduct.getId()).get();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(originalStock); // 원래대로 복구

        // 장바구니 유지 확인
        Cart updatedCart = cartRepository.findById(testCart.getId()).get();
        assertThat(updatedCart.getCartItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 생성 실패 // 빈 장바구니")
    void createOrder_Fail_EmptyCart() {
        // 빈 장바구니
        OrderCreateReq request = new OrderCreateReq(
                testUser.getId(),
                "Test delivery address"
        );

        // 예외 발생
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("장바구니가 비어있습니다");
    }

    @Test
    @DisplayName("주문 생성 실패 // 재고 부족")
    void createOrder_Fail_InsufficientStock() {
        // 재고보다 많은 수량 요청
        CartItem cartItem = TestDataFactory.createCartItem(testCart, testProduct, 150); // 재고는 100
        testCart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        OrderCreateReq request = new OrderCreateReq(
                testUser.getId(),
                "Test delivery address"
        );


        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("재고가 부족합니다");
    }

    @Test
    @DisplayName("주문 생성 실패 // 품절 상품")
    void createOrder_Fail_OutOfStockProduct() {
        // 품절 상품
        testProduct.decreaseStock(100);
        productRepository.save(testProduct);

        CartItem cartItem = TestDataFactory.createCartItem(testCart, testProduct, 1);
        testCart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        OrderCreateReq request = new OrderCreateReq(
                testUser.getId(),
                "Test delivery"
        );

        // 예외 발생
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("품절");
    }

    @Test
    @DisplayName("주문 생성 실패 // 존재하지 않는 사용자")
    void createOrder_Fail_UserNotFound() {
        // 존재하지 않는 사용자 ID
        OrderCreateReq request = new OrderCreateReq(
                999L,
                "Test Delivery Address"
        );

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("주문 조회 성공")
    void getOrder_Success() {
        CartItem cartItem = TestDataFactory.createCartItem(testCart, testProduct, 1);
        testCart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        OrderCreateReq request = new OrderCreateReq(
                testUser.getId(),
                "Test Delivery Address"
        );

        PaymentRes paymentResponse = new PaymentRes(
                "SUCCESS",
                "txn_test_789",
                "Payment processed successfully"
        );
        given(paymentClient.processPayment(any())).willReturn(paymentResponse);

        OrderRes createdOrder = orderService.createOrder(request);

        // 주문 조회
        OrderRes result = orderService.getOrder(createdOrder.getOrderId());

        //check!!!
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(createdOrder.getOrderId());
        assertThat(result.getUserId()).isEqualTo(testUser.getId());
        assertThat(result.getTotalAmount()).isEqualTo(10000);
    }

    @Test
    @DisplayName("사용자별 주문 목록 조회")
    void getOrdersByUser_Success() {
        // 여러 주문 생성
        for (int i = 0; i < 3; i++) {
            CartItem cartItem = TestDataFactory.createCartItem(testCart, testProduct, 1);
            testCart.addCartItem(cartItem);
            cartItemRepository.save(cartItem);

            OrderCreateReq request = new OrderCreateReq(
                    testUser.getId(),
                    "Test address " + i
            );

            PaymentRes paymentResponse = new PaymentRes(
                    "SUCCESS",
                    "txn_test_" + i,
                    "Payment processed!"
            );
            given(paymentClient.processPayment(any())).willReturn(paymentResponse);

            orderService.createOrder(request);
        }

        // 주문 목록 조회
        var result = orderService.getOrdersByUser(testUser.getId());

        // check!
        assertThat(result).hasSize(3);
        assertThat(result).allMatch(order -> order.getUserId().equals(testUser.getId()));
    }
}