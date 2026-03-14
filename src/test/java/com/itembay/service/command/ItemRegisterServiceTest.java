package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemRegisterReqData;
import com.itembay.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@DisplayName("아이템 등록 Service 테스트")
class ItemRegisterServiceTest {

    @Autowired
    ItemCommandService itemCommandService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("아이템 등록 성공")
    void register_item() {
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
        itemCommandService.registerItem(newItem);

        // when
        List<Item> itemList = itemRepository.findAll();
        Item firstItem = itemList.getFirst();

        //then
        assert firstItem.getSellerName().equals(sellerName);
    }

    @Test
    @DisplayName("아이템 등록 실패 - 가격 음수")
    void register_item_failed_price_negative() {
        // given
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(-25470);
        int quantity = 3000;

        ItemRegisterReqData newItem = ItemRegisterReqData.builder()
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        // when and then
        assertThatThrownBy(() -> itemCommandService.registerItem(newItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거래 가격은 필수이고 0보다 커야 합니다.");
    }
}
