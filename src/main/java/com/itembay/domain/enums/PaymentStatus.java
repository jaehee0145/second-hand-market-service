package com.itembay.domain.enums;

public enum PaymentStatus {
    READY("enum_ready", "준비"),
    SUCCESS("enum_success", "성공"),
    FAILED("enum_failed", "실패"),
    CANCELLED("enum_cancelled", "취소");

    private final String key;
    private final String description;

    PaymentStatus(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
