package com.itembay.dto;

import com.itembay.domain.enums.ItemType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemRegisterReqData(
        String server,
        String sellerName,
        ItemType itemType,
        String title,
        BigDecimal price,
        int quantity
) {
}
