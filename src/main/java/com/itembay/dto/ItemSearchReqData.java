package com.itembay.dto;

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
        int page,
        int size
) {
}
