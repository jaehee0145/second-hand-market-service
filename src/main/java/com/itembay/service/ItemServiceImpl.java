package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.domain.QItem;
import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemSearchReqData;
import com.itembay.repository.ItemRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final JPAQueryFactory jpaQueryfactory;
    private static final QItem ITEM = QItem.item;

    @Override
    public Item registerItem(ItemRegisterReqData req) {

        if (req.server() == null || req.server().isBlank()) {
            throw new IllegalArgumentException("서버 이름은 필수입니다.");
        }

        if (req.sellerName() == null || req.sellerName().isBlank()) {
            throw new IllegalArgumentException("판매자 닉네임은 필수입니다.");
        }

        if (req.itemType() == null) {
            throw new IllegalArgumentException("상품 종류는 필수입니다.");
        }

        if (req.title() == null || req.title().isBlank()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }

        if (req.price() == null || req.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("거래 가격은 필수이고 0보다 커야 합니다.");
        }

        if (req.quantity() < 0) {
            throw new IllegalArgumentException("판매 수량은 0보다 커야 합니다.");
        }

        Item newItem = Item.builder()
                .server(req.server())
                .sellerName(req.sellerName())
                .itemType(req.itemType())
                .title(req.title())
                .price(req.price())
                .quantity(req.quantity())
                .build();
        return itemRepository.save(newItem);
    }

    @Override
    public Page<Item> searchItem(ItemSearchReqData req) {
        log.info("ItemSearchReqData : {}", req);
        Pageable pageable = PageRequest.of(req.page(), req.size());

        JPAQuery<Item> contentQuery = jpaQueryfactory.selectFrom(ITEM)
                .where(titleContains(req.title()),
                        priceBetween(req.minPrice(), req.maxPrice()),
                        itemTypeEq(req.itemType()),
                        serverEq(req.server()))
                .limit(req.size()).offset(pageable.getOffset())
                .orderBy(getSortOption(req));

        JPAQuery<Long> countQuery = jpaQueryfactory
                .select(ITEM.count())
                .from(ITEM)
                .where(titleContains(req.title()),
                        priceBetween(req.minPrice(), req.maxPrice()),
                        itemTypeEq(req.itemType()),
                        serverEq(req.server()));

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

    private BooleanExpression itemTypeEq(ItemType itemType) {
        return itemType != null ? ITEM.itemType.eq(itemType) : null;
    }

    private BooleanExpression serverEq(String server) {
        return StringUtils.hasText(server) ? ITEM.server.eq(server) : null;
    }

    private static OrderSpecifier<?> getSortOption(ItemSearchReqData req) {
        ItemSortType itemSortType = req.itemSortType();
        return switch (itemSortType) {
            case PRICE_ASC -> ITEM.price.asc();
            case PRICE_DESC -> ITEM.price.desc();
            case CREATED_DESC -> ITEM.createdAt.desc();
            case CREATED_ASC -> ITEM.createdAt.asc();
        };
    }
}
