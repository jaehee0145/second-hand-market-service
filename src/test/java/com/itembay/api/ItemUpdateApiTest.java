package com.itembay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itembay.domain.Item;
import com.itembay.domain.enums.Category;
import com.itembay.dto.ItemUpdateReqData;
import com.itembay.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("아이템 수정 API 테스트")
class ItemUpdateApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
    void update_item_succeeded() throws Exception {
        // given
        ItemUpdateReqData req = ItemUpdateReqData.builder()
                .id(savedItemId)
                .sellerName("테스트")
                .category(Category.ELECTRONICS)
                .title("수정된 제목")
                .price(new BigDecimal("20000"))
                .quantity(5)
                .build();

        // when and then
        mockMvc.perform(put("/api/items/{itemId}", savedItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(savedItemId)))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 수정 실패 - 유효하지 않은 ID")
    void update_item_failed_invalid_id() throws Exception {
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
        mockMvc.perform(put("/api/items/{itemId}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("해당 아이템을 찾을 수 없습니다. ID: 9999"))
                .andDo(print());
    }
}
