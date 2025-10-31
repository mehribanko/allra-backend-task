package com.allra.allrabackendtask.market.domain.order.service;


import com.allra.allrabackendtask.market.domain.cart.entity.Cart;
import com.allra.allrabackendtask.market.domain.cart.entity.CartItem;
import com.allra.allrabackendtask.market.domain.cart.repo.CartItemRepository;
import com.allra.allrabackendtask.market.domain.cart.repo.CartRepository;
import com.allra.allrabackendtask.market.domain.order.dto.OrderCreateReq;
import com.allra.allrabackendtask.market.domain.order.dto.OrderRes;
import com.allra.allrabackendtask.market.domain.order.entity.Order;
import com.allra.allrabackendtask.market.domain.order.entity.OrderItem;
import com.allra.allrabackendtask.market.domain.order.entity.OrderStatus;
import com.allra.allrabackendtask.market.domain.order.repo.OrderRepository;
import com.allra.allrabackendtask.market.domain.payment.dto.PaymentReq;
import com.allra.allrabackendtask.market.domain.payment.dto.PaymentRes;
import com.allra.allrabackendtask.market.domain.payment.entity.Payment;
import com.allra.allrabackendtask.market.domain.payment.repo.PaymentRepository;
import com.allra.allrabackendtask.market.domain.payment.service.PaymentClient;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.user.entity.User;
import com.allra.allrabackendtask.market.domain.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;

    /**
     * 주문 생성 및 결제 처리
     */
    @Transactional
    public OrderRes createOrder(OrderCreateReq request) {
        // 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다!!" + request.getUserId()));

        // 장바구니 조회
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다!!"));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("장바구니가 비어있습니다.");
        }

        // 재고 확인
        validateStock(cart.getCartItems());

        // 주문 생성
        Order order = Order.builder()
                .user(user)
                .totalAmount(cart.getTotalPrice())
                .deliveryAddress(request.getDeliveryAddress())
                .status(OrderStatus.PENDING)
                .build();

        // 주문  생성 그리고 결재된 가격을 저장하기!
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice())
                    .build();

            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);
        log.info("========================= Order Service ==========================");
        log.info("주문 생성 id  {}", order.getId());

        // 재고 차감
        decreaseStock(cart.getCartItems());

        // 결제 처리
        order.updateStatus(OrderStatus.PAYMENT_PENDING);
        PaymentRes paymentResponse = processPayment(order);

        // 결제 결과에 따라 주문 상태 업데이트
        if ("SUCCESS".equals(paymentResponse.getStatus())) {
            order.completePayment();
            log.info("========================= Order Service ==========================");
            log.info("결제 성공 주문 상태 업데이트: {}", order.getId());

            // 장바구니 비우기
            cart.clearCart();
            cartItemRepository.deleteByCartId(cart.getId());

        } else {
            order.updateStatus(OrderStatus.CANCELLED);
            restoreStock(order.getOrderItems());
            log.info("========================= Order Service ==========================");
            log.info("결제 실패 주문 상태 업데이트: {}", order.getId());
        }

        return OrderRes.from(order);
    }

    /**
     * 주문 조회
     */
    public List<OrderRes> getOrdersByUser(Long userId) {
        log.info("========================= Order Service ==========================");
        log.info(" 주문 조회 사용자 id {}", userId);

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(OrderRes::from)
                .toList();
    }

    /**
     * 주문 상세 조회
     */
    public OrderRes getOrder(Long orderId) {
        log.info("========================= Order Service ==========================");
        log.info(" 주문 상세 조회 id {}", orderId);

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));

        return OrderRes.from(order);
    }

    /**
     * 재고 확인
     */
    private void validateStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            if (!product.getIsAvailable()) {
                throw new IllegalStateException(
                        String.format("상품이 품절되었습니다 -> ", product.getName())
                );
            }
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException(
                        String.format("재고가 부족합니다.", item.getQuantity())
                );
            }
        }
    }

    /**
     * 재고 차감
     */
    private void decreaseStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            product.decreaseStock(item.getQuantity());
        }
    }

    /**
     * 재고 복구
     */
    private void restoreStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            product.increaseStock(item.getQuantity());
        }
    }

    /**
     * 결제 처리
     */
    private PaymentRes processPayment(Order order) {
        // 결제 요청
        PaymentReq paymentRequest = PaymentReq.builder()
                .orderId(order.getId().toString())
                .amount(order.getTotalAmount())
                .build();

        // 외부 결제 api 호출
        PaymentRes paymentResponse = paymentClient.processPayment(paymentRequest);

        // 결제 엔티티 생성
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .build();

        if ("SUCCESS".equals(paymentResponse.getStatus())) {
            payment.completePayment(paymentResponse.getTransactionId());
        } else {
            payment.failPayment(paymentResponse.getMessage());
        }

        paymentRepository.save(payment);
        return paymentResponse;
    }
}
