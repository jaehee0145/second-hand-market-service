package com.itembay.dto;

import com.itembay.domain.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemRegisterReqData(

        @NotBlank(message = "판매자 닉네임은 필수입니다.")
        String sellerName,

        @NotNull(message = "카테고리는 필수입니다.")
        Category category,

        @NotBlank(message = "상품명은 필수입니다.")
        String title,

        @NotNull(message = "거래 가격은 필수이고 0보다 커야 합니다.")
        @Positive(message = "거래 가격은 필수이고 0보다 커야 합니다.")
        BigDecimal price,

        @NotNull(message = "판매 수량은 필수이고 0보다 커야 합니다.")
        @Positive(message = "판매 수량은 필수이고 0보다 커야 합니다.")
        int quantity
) {
}
