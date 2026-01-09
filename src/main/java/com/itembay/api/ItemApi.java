package com.itembay.api;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemApi {

    private final ItemService itemService;

    @PostMapping("/api/items")
    public ResponseEntity registerItem(@RequestBody ItemRegisterReqData req) {
        Item newItem = itemService.registerItem(req);
        // TODO. Jaehee Park 26.01.09 엔티티 대신 DTO 반환하도록 수정 예정
        return ResponseEntity.status(201).body(newItem);
    }
}
