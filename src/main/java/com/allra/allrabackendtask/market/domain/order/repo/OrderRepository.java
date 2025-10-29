package com.allra.allrabackendtask.market.domain.order.repo;

import com.allra.allrabackendtask.market.domain.order.entity.Order;
import com.allra.allrabackendtask.market.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Order o JOIN FETCH o.payment WHERE o.id = :orderId")
    Optional<Order> findByIdWithPayment(@Param("orderId") Long orderId);
}