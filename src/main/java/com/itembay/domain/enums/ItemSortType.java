package com.itembay.domain.enums;

public enum ItemSortType {
    PRICE_ASC("enum_price_asc", "가격 오름차순"),
    PRICE_DESC("enum_price_desc", "가격 내림차순"),
    CREATED_ASC("enum_created_asc", "생성일 오름차순"),
    CREATED_DESC("enum_created_desc", "생성일 내림차순");

    private final String key;
    private final String description;

    ItemSortType(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
