package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemUpdateReqData;
import com.itembay.error.ItemNotFoundException;
import com.itembay.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@DisplayName("아이템 수정 Service 테스트")
class ItemUpdateServiceTest {

    @Autowired
    ItemCommandService itemCommandService;

    @Autowired
    ItemRepository itemRepository;

    private Long savedItemId;

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .server("라엘01")
                .sellerName("테스터")
                .itemType(ItemType.ITEM)
                .title("기존 제목")
                .price(new BigDecimal("10000"))
                .quantity(10)
                .build();
        savedItemId = itemRepository.save(item).getId();
    }

    @Test
    @DisplayName("아이템 수정 성공")
    void update_item_succeeded() {
        // given
        ItemUpdateReqData req = ItemUpdateReqData.builder()
                .id(savedItemId)
                .server("라엘02")
                .sellerName("테스트")
                .itemType(ItemType.GAME_MONEY)
                .title("수정된 제목")
                .price(new BigDecimal("20000"))
                .quantity(5)
                .build();

        // when
        itemCommandService.updateItem(req);

        // then
        Item updatedItem = itemRepository.findById(savedItemId).orElseThrow();
        assertThat(updatedItem.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedItem.getPrice()).isEqualByComparingTo("20000");
        assertThat(updatedItem.getServer()).isEqualTo("라엘02");
        assertThat(updatedItem.getQuantity()).isEqualTo(5);
        assertThat(updatedItem.getItemType()).isEqualTo(ItemType.GAME_MONEY);
    }

    @Test
    @DisplayName("아이템 수정 실패 - 존재하지 않는 ID")
    void update_item_failed_invalid_id() {
        // given
        Long invalidId = 9999L;
        ItemUpdateReqData req = ItemUpdateReqData.builder()
                .id(invalidId)
                .server("라엘02")
                .sellerName("테스트")
                .itemType(ItemType.GAME_MONEY)
                .title("수정된 제목")
                .price(new BigDecimal("20000"))
                .quantity(5)
                .build();

        // when and then
        assertThatThrownBy(() -> itemCommandService.updateItem(req))
                .isInstanceOf(ItemNotFoundException.class);
    }
}
