package com.itembay.dto;

import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemSearchReqData(

        @NotBlank(message = "상품명은 필수입니다.")
        String title,

        @NotBlank(message = "서버 이름은 필수입니다.")
        String server,

        @NotNull(message = "상품 종류는 필수입니다.")
        ItemType itemType,

        BigDecimal minPrice,
        BigDecimal maxPrice,
        ItemSortType itemSortType,
        int page,
        int size
) {
}
