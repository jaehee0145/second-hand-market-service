package com.itembay.domain.enums;

public enum Category {
    ELECTRONICS("enum_electronics", "전자제품"),
    FASHION("enum_fashion", "패션"),
    BOOK("enum_book", "책"),
    ETC("enum_etc", "기타");

    private final String key;
    private final String description;

    Category(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
