package com.itembay.repository;

import com.itembay.domain.Item;
import com.itembay.domain.enums.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@SpringBootTest
@DisplayName("아이템 Repository 테스트")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("아이템 등록 성공")
    void register_item() {
        // given
        String sellerName = "아리";
        Category category = Category.ELECTRONICS;
        String title = "다야 팝니다 필요하신만큼 신청해주세요";
        BigDecimal price = new BigDecimal(25470);
        int quantity = 3000;

        Item newItem = Item.builder()
                .sellerName(sellerName)
                .category(category)
                .title(title)
                .price(price)
                .quantity(quantity)
                .build();


        // when
        Item savedItem = itemRepository.save(newItem);

        //then
        assert savedItem.getSellerName().equals(sellerName);
    }
}
