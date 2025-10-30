package com.allra.allrabackendtask.market.domain.cart.controller;


import com.allra.allrabackendtask.market.domain.cart.dto.CartAddReq;
import com.allra.allrabackendtask.market.domain.cart.dto.CartRes;
import com.allra.allrabackendtask.market.domain.cart.dto.CartUpdateItemReq;
import com.allra.allrabackendtask.market.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 조회
     * GET
     */
    @GetMapping
    public ResponseEntity<CartRes> getCart(@RequestParam Long userId) {
        CartRes cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * 장바구니에 상품 추가
     * POST
     */
    @PostMapping
    public ResponseEntity<CartRes> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody CartAddReq request
    ) {

        CartRes cart = cartService.addToCart(userId, request);
        return ResponseEntity.ok(cart);
    }

    /**
     * 장바구니 아이템 수량 수정
     * PUT
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartRes> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam Long userId,
            @Valid @RequestBody CartUpdateItemReq request
    ) {

        CartRes cart = cartService.updateCartItem(userId, cartItemId, request);
        return ResponseEntity.ok(cart);
    }

    /**
     * 장바구니 아이템 삭제
     * DELETE
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartRes> removeCartItem(
            @PathVariable Long cartItemId,
            @RequestParam Long userId
    ) {

        CartRes cart = cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.ok(cart);
    }

    /**
     * 장바구니 전체 비우기
     * DELETE
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}