package com.itembay.domain;

import com.itembay.domain.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String server;

    @Column(nullable = false, length = 50)
    private String sellerName;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "DECIMAL(15,6)")
    private BigDecimal price;

    private int quantity;

    @Builder
    public Item(Long id, String server, String sellerName, ItemType itemType, String title, BigDecimal price, int quantity) {
        super();
        this.id = id;
        this.server = server;
        this.sellerName = sellerName;
        this.itemType = itemType;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public void update(String server, String sellerName, ItemType itemType, String title, BigDecimal price, int quantity) {
        this.server = server;
        this.sellerName = sellerName;
        this.itemType = itemType;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }
}
