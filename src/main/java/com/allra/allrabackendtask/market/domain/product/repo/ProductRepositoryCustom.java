package com.allra.allrabackendtask.market.domain.product.repo;



import com.allra.allrabackendtask.market.domain.product.dto.ProductSearchReq;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> searchProducts(ProductSearchReq searchRequest, Pageable pageable);
}
