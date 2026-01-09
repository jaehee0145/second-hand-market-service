package com.itembay.service;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemSortType;
import com.itembay.domain.enums.ItemType;
import com.itembay.dto.ItemSearchReqData;
import com.itembay.repository.ItemRepository;
import com.itembay.service.query.ItemQueryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ItemCacheTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemQueryService itemQueryService;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 준비 (1000~10000원 사이 10개 데이터 생성)
        for (int i = 1; i <= 10; i++) {
            itemRepository.save(Item.builder()
                    .server("라엘")
                    .sellerName("판매자" + i)
                    .itemType(ItemType.GAME_MONEY)
                    .title("골드 아이템 " + i)
                    .price(new BigDecimal(10000 * i))
                    .quantity(1000)
                    .build());
        }
    }

    @Test
    @DisplayName("동일한 검색 조건으로 조회했을때 두번째 요청은 캐싱된 데이터를 리턴")
    void item_search_cache_test() {
        // given
        ItemSearchReqData req = ItemSearchReqData.builder()
                .title("골드")
                .server("라엘")
                .itemType(ItemType.GAME_MONEY)
                .minPrice(new BigDecimal("1000"))
                .maxPrice(new BigDecimal("5000"))
                .page(0) // 첫 번째 페이지
                .size(3) // 한 페이지에 3개씩
                .itemSortType(ItemSortType.PRICE_DESC)
                .build();

        // when
        System.out.println("== 첫번째 호출 (DB 조회) ==");
        Page<Item> firstResult = itemQueryService.searchItem(req);

        System.out.println("== 두번째 호출 (캐시 사용) ==");
        Page<Item> secondResult = itemQueryService.searchItem(req);

        // then
        // 두 결과의 내용이 동일해야 함
        assertThat(firstResult.getContent().size()).isEqualTo(secondResult.getContent().size());

        // 캐시에 데이터가 저장되어 있는지 확인
        Cache cache = cacheManager.getCache("itemSearchResults");
        assertThat(cache).isNotNull();
        assertThat(cache.get(req.hashCode())).isNotNull();
    }
}
