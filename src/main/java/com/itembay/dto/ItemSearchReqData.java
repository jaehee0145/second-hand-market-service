package com.itembay.dto;

import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.ItemType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemSearchReqData(
        String title,
        String server,
        ItemType itemType,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        ItemSortType itemSortType,
        int page,
        int size
) {
}
