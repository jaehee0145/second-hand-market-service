package com.itembay.api;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.dto.ItemResponse;
import com.itembay.dto.ItemSearchReqData;
import com.itembay.dto.ItemUpdateReqData;
import com.itembay.dto.PageResponse;
import com.itembay.service.command.ItemCommandService;
import com.itembay.service.query.ItemQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemApi {

    private final ItemCommandService itemCommandService;
    private final ItemQueryService itemQueryService;

    @PostMapping("/api/items")
    public ResponseEntity<ItemResponse> registerItem(@Valid @RequestBody ItemRegisterReqData req) {
        Item newItem = itemCommandService.registerItem(req);
        ItemResponse response = ItemResponse.from(newItem);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/api/items")
    public ResponseEntity<PageResponse<ItemResponse>> searchItem(@Valid @ModelAttribute ItemSearchReqData req) {
        Page<Item> itemPage = itemQueryService.searchItem(req);
        Page<ItemResponse> responsePage = itemPage.map(ItemResponse::from);
        return ResponseEntity.ok(PageResponse.of(responsePage));
    }

    @PutMapping("/api/items/{id}")
    public ResponseEntity<Long> updateItem(@Valid @RequestBody ItemUpdateReqData req) {
        Long itemId = itemCommandService.updateItem(req);
        return ResponseEntity.ok(itemId);
    }

    @DeleteMapping("/api/items/{id}")
    public ResponseEntity<Long> deleteItem(@PathVariable Long id) {
        Long deletedItemId = itemCommandService.deleteItem(id);
        return ResponseEntity.ok(deletedItemId);
    }

}
