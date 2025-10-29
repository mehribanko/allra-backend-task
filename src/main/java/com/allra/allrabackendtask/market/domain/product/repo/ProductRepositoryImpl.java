package com.allra.allrabackendtask.market.domain.product.repo;


import com.allra.allrabackendtask.market.domain.product.dto.ProductSearchReq;
import com.allra.allrabackendtask.market.domain.product.entity.Product;
import com.allra.allrabackendtask.market.domain.product.entity.QProduct;
import com.allra.allrabackendtask.market.domain.category.entity.QCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 상품 QueryDSL 검색 구현
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(ProductSearchReq searchRequest, Pageable pageable) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;


        List<Product> products = queryFactory
                .selectFrom(product)
                .leftJoin(product.category, category).fetchJoin()
                .where(
                        categoryIdEq(searchRequest.getCategoryId()),
                        nameContains(searchRequest.getName()),
                        priceBetween(searchRequest.getMinPrice(), searchRequest.getMaxPrice()),
                        isAvailableEq(searchRequest.getIsAvailable())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        categoryIdEq(searchRequest.getCategoryId()),
                        nameContains(searchRequest.getName()),
                        priceBetween(searchRequest.getMinPrice(), searchRequest.getMaxPrice()),
                        isAvailableEq(searchRequest.getIsAvailable())
                );

        return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchOne);
    }


    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? QProduct.product.category.id.eq(categoryId) : null;
    }

    private BooleanExpression nameContains(String name) {
        return name != null && !name.isEmpty() ? QProduct.product.name.containsIgnoreCase(name) : null;
    }

    private BooleanExpression priceBetween(Integer minPrice, Integer maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return QProduct.product.price.between(minPrice, maxPrice);
        } else if (minPrice != null) {
            return QProduct.product.price.goe(minPrice);
        } else if (maxPrice != null) {
            return QProduct.product.price.loe(maxPrice);
        }
        return null;
    }

    private BooleanExpression isAvailableEq(Boolean isAvailable) {
        return isAvailable != null ? QProduct.product.isAvailable.eq(isAvailable) : null;
    }
}