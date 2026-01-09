package com.itembay.service.query;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemSearchReqData;
import com.itembay.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DisplayName("아이템 조회 Service 테스트")
public class ItemSearchServiceTest {

    @Autowired
    ItemQueryService itemQueryService;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void init() {
        // 테스트용 데이터 준비 (서버: 라엘, 가격: 1000~10000원 사이 10개 데이터 생성)
        for (int i = 1; i <= 10; i++) {
            itemRepository.save(Item.builder()
                    .server("라엘service")
                    .sellerName("판매자" + i)
                    .itemType(ItemType.GAME_MONEY)
                    .title("골드 아이템 판매합니다 " + i)
                    .price(new BigDecimal(1000 * i))
                    .quantity(100)
                    .build());
        }
    }

    @Test
    @DisplayName("아이템 조회 성공 - 조건 필터링 및 페이지 응답 검증")
    void search_item_with_filter_and_paging() {
        // given
        // 1000원 ~ 5000원 사이의 아이템을 검색 (데이터 상 5개가 해당됨)
        ItemSearchReqData req = ItemSearchReqData.builder()
                .title("골드")
                .server("라엘service")
                .itemType(ItemType.GAME_MONEY)
                .minPrice(new BigDecimal("1000"))
                .maxPrice(new BigDecimal("5000"))
                .page(0)
                .size(3)
                .itemSortType(ItemSortType.PRICE_DESC)
                .build();

        // when
        Page<Item> result = itemQueryService.searchItem(req);

        // then
        assertThat(result.getTotalElements()).isEqualTo(5); // 전체 조건 일치 개수는 5개
        assertThat(result.getContent().size()).isEqualTo(3); // 현재 페이지 데이터는 3개
        assertThat(result.getTotalPages()).isEqualTo(2);    // 3개씩 2페이지 존재
        assertThat(result.getContent().getFirst().getPrice()).isEqualByComparingTo("5000"); // 가격 내림차순 확인
    }
}
