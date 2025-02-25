package com.shop2.dto;

import com.shop2.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

//조회한 주문 데이터를 화면에 보낼 떄 사용할 dto 클래스를 만듬. 주문 상품 정보를 담을 OrderItemDto 클래스를 생성
@Getter
@Setter
public class OrderItemDto {
    //OrderItemDto 클래스 생성자로 orderItem 객체와 이미지 경로를 파라미터로 받아서 멤버 변수 값을 세팅합니다.
    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
    private String itemNm; //상품명
    private int count; // 주문수량
    private int orderPrice; //주문금액

    private String imgUrl; //상품 이미지 경로
}
