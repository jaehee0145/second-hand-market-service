package com.itembay.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemRegisterReqData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Item API Test")
public class ItemApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("아이템 등록에 성공한다. - 201 응답")
    public void register_item() throws Exception {

        // given
        String server = "라엘08";
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(25470);
        int quantity = 3000;

        ItemRegisterReqData request = ItemRegisterReqData.builder()
                .server(server)
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("다야 팝니다 필요하신만큼 신청해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 등록에 실패한다. - 가격 오류인 경우 400 응답")
    public void register_item_failed_price_error() throws Exception {

        // given
        String server = "라엘08";
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(-25470);
        int quantity = 3000;

        ItemRegisterReqData request = ItemRegisterReqData.builder()
                .server(server)
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("거래 가격은 필수이고 0보다 커야 합니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 등록에 실패한다. - 서버 이름 누락인 경우 400 응답")
    public void register_item_failed_server_name_missing() throws Exception {

        // given
        String sellerName = "아리";
        ItemType itemType = ItemType.GAME_MONEY;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(25470);
        int quantity = 3000;

        ItemRegisterReqData request = ItemRegisterReqData.builder()
                .sellerName(sellerName)
                .itemType(itemType)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("서버 이름은 필수입니다."))
                .andDo(print());
    }
}
