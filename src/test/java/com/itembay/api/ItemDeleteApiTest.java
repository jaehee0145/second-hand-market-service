package com.itembay.api;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;
import com.itembay.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("아이템 삭제 API 테스트")
public class ItemDeleteApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("아이템 삭제 성공")
    void delete_item_succeeded() throws Exception {
        //given
        Item item = Item.builder()
                .server("라엘delete")
                .sellerName("테스터")
                .itemType(ItemType.ITEM)
                .title("아이템delete")
                .price(new BigDecimal("10000"))
                .quantity(10)
                .build();
        Long itemId = itemRepository.save(item).getId();
        em.flush();
        em.clear();

        // when and then
        mockMvc.perform(delete("/api/items/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(itemId)))
                .andDo(print());
    }
}
