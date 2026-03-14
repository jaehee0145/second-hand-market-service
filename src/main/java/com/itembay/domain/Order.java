package com.itembay.domain;

import com.itembay.domain.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    Long itemId;

    @Column(nullable = false)
    Long buyerId;

    @Column(nullable = false)
    Long sellerId;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal price;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

}
