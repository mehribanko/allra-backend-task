package com.allra.allrabackendtask.market.domain.product.repo;

import com.allra.allrabackendtask.market.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom{

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContaining(String name);

    List<Product> findByIsAvailableTrue();
}