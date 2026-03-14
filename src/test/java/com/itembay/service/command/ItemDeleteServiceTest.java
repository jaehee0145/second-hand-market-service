package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;
import com.itembay.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DisplayName("아이템 삭제 Service 테스트")
class ItemDeleteServiceTest {

    @Autowired
    ItemCommandService itemCommandService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("아이템 삭제 성공")
    void delete_item_succeeded() {
        //given
        Item item = Item.builder()
                .sellerName("테스터")
                .itemType(ItemType.ITEM)
                .title("아이템delete")
                .price(new BigDecimal("10000"))
                .quantity(10)
                .build();
        Long savedItemId = itemRepository.save(item).getId();
        em.flush();
        em.clear();

        //when
        itemCommandService.deleteItem(savedItemId);
        em.flush();
        em.clear();

        //then
        assertThat(itemRepository.findById(savedItemId)).isEmpty();
    }
}
