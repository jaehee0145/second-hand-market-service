package com.itembay.api;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.ItemType;
import com.itembay.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("아이템 조회 API 테스트")
class ItemSearchApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 준비 (1000~10000원 사이 10개 데이터 생성)
        for (int i = 1; i <= 10; i++) {
            itemRepository.save(Item.builder()
                    .sellerName("판매자" + i)
                    .itemType(ItemType.GAME_MONEY)
                    .title("골드 아이템 " + i)
                    .price(new BigDecimal(10000 * i))
                    .quantity(1000)
                    .build());
        }
    }

    @Test
    @DisplayName("아이템 조회 성공 - 조건 필터링 및 페이징 응답 검증")
    void search_items_succeeded() throws Exception {
        // given
        // '골드'가 포함된 상품명, 가격 10000원 ~ 60000원 사이 검색 (데이터 상 6개 해당)
        String title = "골드";
        String minPrice = "10000";
        String maxPrice = "60000";

        // when and then
        mockMvc.perform(get("/api/items")
                        .param("title", title)
                        .param("itemType", ItemType.GAME_MONEY.name())
                        .param("itemSortType", ItemSortType.PRICE_ASC.name())
                        .param("minPrice", minPrice)
                        .param("maxPrice", maxPrice)
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("골드 아이템 6"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(6))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 조회 성공 - 빈 결과")
    void search_items_succeeded_empty_list() throws Exception {
        // given
        String title = "골드";
        String minPrice = "10000";
        String maxPrice = "60000";

        // when and then
        mockMvc.perform(get("/api/items")
                        .param("title", title)
                        .param("itemType", ItemType.GAME_MONEY.name())
                        .param("itemSortType", ItemSortType.PRICE_ASC.name())
                        .param("minPrice", minPrice)
                        .param("maxPrice", maxPrice)
                        .param("page", "1")
                        .param("size", "5")
                        .param("sortType", "PRICE_ASC") // 정렬 조건
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andDo(print());
    }
}
