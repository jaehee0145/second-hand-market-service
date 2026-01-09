package com.itembay.domain.enums;

public enum ItemType {
    GAME_MONEY("enum_game_money", "게임 머니"),
    ITEM("enum_item", "아이템"),
    ACCOUNT("enum_account", "계정"),
    ETC("enum_etc", "기타");

    private final String key;
    private final String description;

    ItemType(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
