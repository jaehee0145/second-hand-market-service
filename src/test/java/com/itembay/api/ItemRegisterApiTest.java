package com.itembay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itembay.domain.enums.Category;
import com.itembay.dto.ItemRegisterReqData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("아이템 등록 API 테스트")
class ItemRegisterApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("아이템 등록 성공 - 201 응답")
    void register_item_succeeded() throws Exception {
        // given
        String sellerName = "아리";
        Category category = Category.ELECTRONICS;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(25470);
        int quantity = 3000;

        ItemRegisterReqData request = ItemRegisterReqData.builder()
                .sellerName(sellerName)
                .category(category)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        // when and then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("다야 팝니다 필요하신만큼 신청해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 등록 실패 - 가격 음수인 경우 400 응답")
    void register_item_failed_price_negative() throws Exception {
        // given
        String sellerName = "아리";
        Category category = Category.ELECTRONICS;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(-25470);
        int quantity = 3000;

        ItemRegisterReqData request = ItemRegisterReqData.builder()
                .sellerName(sellerName)
                .category(category)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        // when and then
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("거래 가격은 필수이고 0보다 커야 합니다."))
                .andDo(print());
    }
}
