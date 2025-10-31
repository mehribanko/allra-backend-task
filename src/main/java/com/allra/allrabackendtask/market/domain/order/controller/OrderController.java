package com.allra.allrabackendtask.market.domain.order.controller;

import com.allra.allrabackendtask.market.domain.order.dto.OrderCreateReq;
import com.allra.allrabackendtask.market.domain.order.dto.OrderRes;
import com.allra.allrabackendtask.market.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 및 결제
     */
    @PostMapping
    public ResponseEntity<OrderRes> createOrder(@Valid @RequestBody OrderCreateReq request) {
        OrderRes order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    /**
     * 사용자별 주문 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<OrderRes>> getOrders(@RequestParam Long userId) {
        List<OrderRes> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRes> getOrder(@PathVariable Long orderId) {
        OrderRes order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }
}