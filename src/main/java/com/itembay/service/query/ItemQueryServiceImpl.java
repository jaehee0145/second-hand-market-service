package com.itembay.service.query;

import com.itembay.domain.Item;
import com.itembay.domain.QItem;
import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.Category;
import com.itembay.dto.ItemSearchReqData;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemQueryServiceImpl implements ItemQueryService {

    private final JPAQueryFactory jpaQueryfactory;
    private static final QItem ITEM = QItem.item;

    @Cacheable(
            value = "itemSearchResults",
            key = "#req.hashCode()",
            condition = "#req.page() < 5"
    )
    @Override
    public Page<Item> searchItem(ItemSearchReqData req) {
        log.info("ItemSearchReqData : {}", req);
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        JPAQuery<Item> contentQuery = jpaQueryfactory.selectFrom(ITEM)
                .where(titleContains(req.title()),
                        priceBetween(req.minPrice(), req.maxPrice()),
                        categoryEq(req.category()))
                .limit(req.getSize()).offset(pageable.getOffset())
                .orderBy(getSortOption(req));

        JPAQuery<Long> countQuery = jpaQueryfactory
                .select(ITEM.count())
                .from(ITEM)
                .where(titleContains(req.title()),
                        priceBetween(req.minPrice(), req.maxPrice()),
                        categoryEq(req.category()));

        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? ITEM.title.contains(title) : null;
    }

    private BooleanExpression priceBetween(BigDecimal min, BigDecimal max) {
        if (min == null && max == null) return null;
        if (min == null) return ITEM.price.loe(max);
        if (max == null) return ITEM.price.goe(min);
        return ITEM.price.between(min, max);
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? ITEM.category.eq(category) : null;
    }

    private static OrderSpecifier<?> getSortOption(ItemSearchReqData req) {
        ItemSortType itemSortType = req.itemSortType();
        if (itemSortType == null) return ITEM.createdAt.desc();
        return switch (itemSortType) {
            case PRICE_ASC -> ITEM.price.asc();
            case PRICE_DESC -> ITEM.price.desc();
            case CREATED_DESC -> ITEM.createdAt.desc();
            case CREATED_ASC -> ITEM.createdAt.asc();
        };
    }
}
