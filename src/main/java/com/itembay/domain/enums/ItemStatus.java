package com.itembay.domain.enums;

public enum ItemStatus {
    SALE("enum_sale", "판매중"),
    RESERVED("enum_reserved", "예약중"),
    SOLD("enum_sold", "판매완료");

    private final String key;
    private final String description;

    ItemStatus(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
