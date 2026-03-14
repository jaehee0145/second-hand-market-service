package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.domain.enums.Category;
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
                .sellerName("테스터")
                .category(Category.FASHION)
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
                .sellerName("테스트")
                .category(Category.ELECTRONICS)
                .title("수정된 제목")
                .price(new BigDecimal("20000"))
                .quantity(5)
                .build();

        // when
        itemCommandService.updateItem(savedItemId, req);

        // then
        Item updatedItem = itemRepository.findById(savedItemId).orElseThrow();
        assertThat(updatedItem.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedItem.getPrice()).isEqualByComparingTo("20000");
        assertThat(updatedItem.getQuantity()).isEqualTo(5);
        assertThat(updatedItem.getCategory()).isEqualTo(Category.ELECTRONICS);
    }

    @Test
    @DisplayName("아이템 수정 실패 - 존재하지 않는 ID")
    void update_item_failed_invalid_id() {
        // given
        Long invalidId = 9999L;
        ItemUpdateReqData req = ItemUpdateReqData.builder()
                .id(invalidId)
                .sellerName("테스트")
                .category(Category.ELECTRONICS)
                .title("수정된 제목")
                .price(new BigDecimal("20000"))
                .quantity(5)
                .build();

        // when and then
        assertThatThrownBy(() -> itemCommandService.updateItem(invalidId, req))
                .isInstanceOf(ItemNotFoundException.class);
    }
}
