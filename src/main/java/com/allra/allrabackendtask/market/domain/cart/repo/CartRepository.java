package com.allra.allrabackendtask.market.domain.cart.repo;

import com.allra.allrabackendtask.market.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);

    boolean existsByUserId(Long userId);
}