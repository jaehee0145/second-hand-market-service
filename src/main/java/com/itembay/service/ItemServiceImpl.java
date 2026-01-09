package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.domain.QItem;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemSearchReqData;
import com.itembay.repository.ItemRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        Pageable pageable = PageRequest.of(req.page(), req.size());

        JPAQuery<Item> contentQuery = jpaQueryfactory.selectFrom(ITEM)
                .where(ITEM.server.contains(req.server())
                        .and(ITEM.price.between(req.minPrice(), req.maxPrice()))
                        .and(ITEM.itemType.eq(req.itemType())
                        ))
                .limit(req.size()).offset(pageable.getOffset());
        // TODO. Jaehee Park 26.01.09 가격, 생성일 순 정렬

        JPAQuery<Long> countQuery = jpaQueryfactory
                .select(ITEM.count())
                .from(ITEM)
                .where(ITEM.server.contains(req.server())
                        .and(ITEM.price.between(req.minPrice(), req.maxPrice()))
                        .and(ITEM.itemType.eq(req.itemType()))
                );

        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }
}
