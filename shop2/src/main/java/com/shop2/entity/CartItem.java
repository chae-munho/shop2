package com.shop2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 장바구니에는 여러 개의 상품을 담을 수 있으므로 manytoone 어노테이션 사용 다대일 관계로 매핑 즉 CartItem 다 : Cart 일
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
}
