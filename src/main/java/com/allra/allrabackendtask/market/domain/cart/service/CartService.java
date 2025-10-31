package com.allra.allrabackendtask.market.domain.cart.service;


import com.allra.allrabackendtask.market.domain.cart.dto.CartAddReq;
import com.allra.allrabackendtask.market.domain.cart.dto.CartRes;
import com.allra.allrabackendtask.market.domain.cart.dto.CartUpdateItemReq;
import com.allra.allrabackendtask.market.domain.cart.entity.Cart;
import com.allra.allrabackendtask.market.domain.cart.entity.CartItem;
import com.allra.allrabackendtask.market.domain.cart.repo.CartItemRepository;
import com.allra.allrabackendtask.market.domain.cart.repo.CartRepository;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.product.repo.ProductRepository;
import com.allra.allrabackendtask.market.domain.user.entity.User;
import com.allra.allrabackendtask.market.domain.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * 장바구니 조회
     */
    public CartRes getCart(Long userId) {
        log.info(" ======================== CartService =========================");
        log.info("장바구니 조회 (사용자 id): {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

                    return Cart.builder()
                            .id(null)
                            .user(user)
                            .build();
                });

        return CartRes.from(cart);
    }

    /**
     * 장바구니에 상품 추가
     */
    @Transactional
    public CartRes addToCart(Long userId, CartAddReq request) {
        log.info(" ======================== CartService =========================");
        log.info("장바구니에 상품 (추가 상품 id) {}  (사용자 id) : {}", request.getProductId(), userId);

        // 1. 장바구니 조회/ 생성 (장바구니 없으면 생성하는걸로)
        Cart cart = getOrCreateCart(userId);

        // 2. 상품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + request.getProductId()));

        // 3. 상품 재고 확인
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity());
        }

        // 4. 이미 장바구니에 있는 상품인지 확인
        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(
                        // 이미 있으면 수량 증가
                        existingItem -> {
                            int newQuantity = existingItem.getQuantity() + request.getQuantity();
                            if (product.getStockQuantity() < newQuantity) {
                                throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + product.getStockQuantity());
                            }
                            existingItem.updateQuantity(newQuantity);
                            log.info("이미 존재하는 상품 수량을 늘리고 : {}", newQuantity);
                        },
                        // 없으면 새로 추가
                        () -> {
                            CartItem newItem = CartItem.builder()
                                    .cart(cart)
                                    .product(product)
                                    .quantity(request.getQuantity())
                                    .build();
                            cart.addCartItem(newItem);
                            cartItemRepository.save(newItem);
                            log.info("장바구니에 상품 추가 완료");
                        }
                );

        return CartRes.from(cart);
    }

    /**
     * 장바구니 item 수량 수정
     */
    @Transactional
    public CartRes updateCartItem(Long userId, Long cartItemId, CartUpdateItemReq request) {
        log.info(" ======================== CartService =========================");
        log.info("장바구니 item 수량 수정  (장바구니 상품 id) {} (사용자 id): {}", cartItemId, userId);

        // 1. 장바구니 조회
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        // 2. 장바구니 item 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 item 찾을 수 없습니다. ID: " + cartItemId));

        // 3. 해당 item 사용자의 장바구니에 속하는지 확인
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("해당 장바구니 item 접근할 수 없습니다.");
        }

        // 4. 재고 확인
        if (cartItem.getProduct().getStockQuantity() < request.getQuantity()) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + cartItem.getProduct().getStockQuantity());
        }

        // 5. 수량 업데이트
        cartItem.updateQuantity(request.getQuantity());

        return CartRes.from(cart);
    }

    /**
     * 장바구니 item 삭제
     */
    @Transactional
    public CartRes removeCartItem(Long userId, Long cartItemId) {
        log.info(" ======================== CartService =========================");
        log.info("장바구니 item 삭제 (장바구니 상품 id) {} (사용자 id): {}", cartItemId, userId);

        // 1. 장바구니 조회
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        // 2. 장바구니 item 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템을 찾을 수 없습니다. ID: " + cartItemId));

        // 3. 해당 item 사용자의 장바구니에 속하는지 확인
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("해당 장바구니 아이템 접근할 수 없습니다.");
        }

        // 4. 삭제
        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);

        return CartRes.from(cart);
    }

    /**
     * 장바구니 전체 비우기
     */
    @Transactional
    public void clearCart(Long userId) {
        log.info(" ======================== CartService =========================");
        log.info("장바구니 전체 비우기 (사용자 id): {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        cart.clearCart();
        cartItemRepository.deleteByCartId(cart.getId());
    }

    /**
     * 장바구니 조회 또는 생성
     */
    @Transactional
    protected Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);
                });
    }
}