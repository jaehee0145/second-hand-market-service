package com.itembay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCreateReqData(

        @NotNull(message = "구매 Item ID는 필수입니다.")
        Long itemId,

        @NotNull(message = "구매 Item ID는 필수입니다.")
        Long buyerId,

        @NotNull(message = "구매 수량은 필수이고 0보다 커야 합니다.")
        @Positive(message = "구매 수량은 필수이고 0보다 커야 합니다.")
        int quantity

) {
}
