package com.shop2.dto;

import com.shop2.constant.OrderStatus;
import com.shop2.entity.Order;
import com.shop2.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//주문 정보를 담을 OrderHistDto 클래스 생성
@Getter
@Setter
public class OrderHistDto {
    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        //OrderHistDto 클래스 생성자로 order 객체를 파라미터로 받아서 멤버 변수 값을 세팅. 주문 날짜의 경우 화면에 yyyy-MM-dd HH:mm 형태로 전달하기 위해서 포맷을 수정한다.
        this.orderDate =
                order.getOrderDate().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }
    private Long orderId; // 주문 아이디
    private String orderDate; //주문 날씨
    private OrderStatus orderStatus; // 주문상태

    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<OrderItemDto>();

    //orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
