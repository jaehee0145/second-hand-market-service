package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@DisplayName("Item Service Test")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("아이템 등록에 성공한다.")
    public void register_item() {

        // given
        String server = "라엘08";
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(25470);
        int quantity = 3000;

        ItemRegisterReqData newItem = ItemRegisterReqData.builder()
                .server(server)
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        itemService.registerItem(newItem);

        // when
        List<Item> itemList = itemRepository.findAll();
        Item firstItem = itemList.getFirst();

        //then
        assert firstItem.getServer().equals(server);
    }

}
