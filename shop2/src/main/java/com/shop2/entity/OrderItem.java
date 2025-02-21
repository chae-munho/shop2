package com.shop2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격

    private int count; // 수량

    //private LocalDateTime regTime;

    //private LocalDateTime updateTime;

    //주문할 상품과 주문 수량을 통해 OrderItem 객체를 만드는 메소드작성
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();

        //주문할 상품과 주문 수량을 세팅한다
        orderItem.setItem(item);
        orderItem.setCount(count);

        //현재 시간 기준으로 상품 가격을 주문 가격으로 세팅. 상품 가격은 시간에 따라서 달라질 수 있음 또한 쿠폰이나 할인을 적용하는 케이스들도 있지만 여기서는 고려하지 않음
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }
    public int getTotalPrice() {
        return orderPrice * count;  // 주문 가격과 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드
    }

}
