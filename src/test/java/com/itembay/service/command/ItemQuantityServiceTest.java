package com.itembay.service.command;

import com.itembay.domain.Item;
import com.itembay.domain.enums.ItemType;
import com.itembay.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemQuantityServiceTest {

    @Autowired
    ItemCommandService itemCommandService;
    @Autowired
    ItemRepository itemRepository;

    private Long savedItemId;

    @BeforeEach
    void setUp() {
        // 초기 재고를 10개로 설정
        Item item = itemRepository.save(Item.builder()
                .server("라엘01")
                .sellerName("테스터")
                .itemType(ItemType.ITEM)
                .title("테스트 아이템")
                .price(new BigDecimal("10000"))
                .quantity(10) // 초기값 10
                .build());
        savedItemId = item.getId();
    }

    @AfterEach
    void clear() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 10개씩 2번 차감을 시도하면, 첫번째 시도는 성공, 두번째 시도는 수량 부족으로 실패")
    void decrease_quantity_concurrency_test() {
        // given
        int decreaseAmount = 10;

        // when
        // 두 개의 스레드에서 동시에 10개씩 차감 시도
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() ->
                itemCommandService.decreaseQuantity(savedItemId, decreaseAmount));

        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() ->
                itemCommandService.decreaseQuantity(savedItemId, decreaseAmount));

        try {
            CompletableFuture.allOf(task1, task2).join();
        } catch (CompletionException e) { // CompletableFuture에서 발생한 예외는 항상 CompletionException
            System.out.println("예상된 예외 발생: " + e.getCause().getMessage());
        }

        // then
        Item result = itemRepository.findById(savedItemId).orElseThrow();
        assertThat(result.getQuantity()).isEqualTo(0);
    }
}
