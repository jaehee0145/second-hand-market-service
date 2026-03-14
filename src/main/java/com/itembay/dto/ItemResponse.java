package com.itembay.dto;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String sellerName,
        ItemType itemType,
        String title,
        BigDecimal price,
        int quantity,
        LocalDateTime createdAt
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getSellerName(),
                item.getItemType(),
                item.getTitle(),
                item.getPrice(),
                item.getQuantity(),
                item.getCreatedAt()
        );
    }
}