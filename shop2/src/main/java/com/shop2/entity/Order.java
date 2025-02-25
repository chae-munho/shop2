package com.shop2.entity;


import com.shop2.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//다대다 매핑을 사용하지 않는 이뉴는 연결 테이블에는 컬럼을 추가할 수 없기 때문.
@Entity
@Table(name = "orders")  // 정렬할 때 order 키워드가 있기 때문에 Order 엔티티에 매핑되는 테이블로 orders를 지정한다.
@Getter
@Setter
public class Order extends BaseEntity{
    @Id
    @Column(name = "order_id")
    @GeneratedValue   //@GeneratedValue 기본값은 (strategy=Generation.AUTO) AUto는 데이터베이스의 identity, sequence, table 전략 중 적절한 것을 자동으로 선택하도록 한다.
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 한 명의 회원은 여러 번 주문할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 한다. many : order , one : member
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태 order : 주문, cancel : 주문취소

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)  // 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CacadeTypeAll 옵션 설정
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();



    //private LocalDateTime regTime;

    //private LocalDateTime updateTime;

    //생성한 주문상품 객체(OrderItem)이용하여 주문 객체를 만드는 메소드를 작성
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        //주문 상태를 order로 세팅한다.
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //총 금액을 구하는 메소드
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return  totalPrice;
    }
    //주문 상태를 취소 상태로 바꿔주는 메소드
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
