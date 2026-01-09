package com.itembay.error;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long itemId) {
        super(String.format("해당 아이템을 찾을 수 없습니다. ID: %d", itemId));
    }
}
