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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("아이템 등록에 실패한다. - 서버 이름 누락")
    public void register_item_failed_server_name_missing() {

        // given
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(25470);
        int quantity = 3000;

        ItemRegisterReqData newItem = ItemRegisterReqData.builder()
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        // when and then
        assertThatThrownBy(() -> itemService.registerItem(newItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("서버 이름은 필수입니다.");
    }

    @Test
    @DisplayName("아이템 등록에 실패한다. - 가격 오류")
    public void register_item_failed_price_error() {

        // given
        String server = "라엘08";
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(-25470);
        int quantity = 3000;

        ItemRegisterReqData newItem = ItemRegisterReqData.builder()
                .server(server)
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        // when and then
        assertThatThrownBy(() -> itemService.registerItem(newItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거래 가격은 필수이고 0보다 커야 합니다.");
    }
}
