package com.itembay.domain;

import com.itembay.domain.enums.Category;
import com.itembay.error.InvalidQuantityException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String sellerName;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "DECIMAL(15,6)")
    private BigDecimal price;

    @PositiveOrZero
    @Column(nullable = false)
    private int quantity;

    @Builder
    public Item(Long id, String sellerName, Category category, String title, BigDecimal price, int quantity) {
        super();
        this.id = id;
        this.sellerName = sellerName;
        this.category = category;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public void update(String sellerName, Category category, String title, BigDecimal price, int quantity) {
        this.sellerName = sellerName;
        this.category = category;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public Long decreaseQuantity(int amount) {
        if (amount <= 0) {
            throw new InvalidQuantityException("차감 수량은 0보다 커야 합니다.");
        }
        if (this.quantity < amount) {
            throw new InvalidQuantityException("아이템 수량이 부족합니다.");
        }
        this.quantity -= amount;
        return this.id;
    }
}
