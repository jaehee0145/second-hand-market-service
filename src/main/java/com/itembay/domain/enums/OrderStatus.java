package com.itembay.domain.enums;

public enum OrderStatus {
    CREATED("enum_created", "주문 생성"),
    PAYMENT_PENDING("enum_payment_pending", "결제 진행 중"),
    PAID("enum_paid", "결제 완료"),
    CANCELLED("enum_cancelled", "주문 취소"),
    COMPLETED("enum_completed", "거래 완료");

    private final String key;
    private final String description;

    OrderStatus(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
