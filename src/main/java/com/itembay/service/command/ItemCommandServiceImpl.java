package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemUpdateReqData;
import com.itembay.error.ItemNotFoundException;
import com.itembay.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@CacheConfig(cacheNames = "itemSearchResults")
public class ItemCommandServiceImpl implements ItemCommandService {

    private final ItemRepository itemRepository;

    @Override
    @CacheEvict(allEntries = true)
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
    @CacheEvict(allEntries = true)
    public Long updateItem(Long itemId, ItemUpdateReqData req) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        item.update(req.server(), req.sellerName(), req.itemType(), req.title(), req.price(), req.quantity());
        return item.getId();
    }

    @Override
    public Long deleteItem(Long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        itemRepository.deleteById(itemId);
        return itemId;
    }
}
