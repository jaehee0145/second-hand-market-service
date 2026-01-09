package com.itembay.api;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemResponse;
import com.itembay.dto.ItemSearchReqData;
import com.itembay.dto.PageResponse;
import com.itembay.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemApi {

    private final ItemService itemService;

    @PostMapping("/api/items")
    public ResponseEntity<ItemResponse> registerItem(@Valid @RequestBody ItemRegisterReqData req) {
        Item newItem = itemService.registerItem(req);
        // TODO. Jaehee Park 26.01.09 엔티티 대신 DTO 반환하도록 수정 예정
        ItemResponse response = ItemResponse.from(newItem);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/api/items")
    public ResponseEntity<PageResponse<ItemResponse>> searchItem(@Valid @ModelAttribute ItemSearchReqData req) {
        Page<Item> itemPage = itemService.searchItem(req);
        Page<ItemResponse> responsePage = itemPage.map(ItemResponse::from);
        return ResponseEntity.ok(PageResponse.of(responsePage));
    }

}
