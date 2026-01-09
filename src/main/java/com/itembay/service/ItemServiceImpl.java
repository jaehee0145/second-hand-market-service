package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item registerItem(ItemRegisterReqData req) {

        Item newItem = Item.builder()
                .server(req.server())
                .sellerName(req.sellerName())
                .itemType(req.itemType())
                .title(req.title())
                .price(req.price())
                .quantity(req.quantity())
                .build();
        return itemRepository.save(newItem);
    }
}
