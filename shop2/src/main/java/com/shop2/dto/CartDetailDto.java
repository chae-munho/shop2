package com.shop2.dto;

//장바구니에 들어있는 상품들을 조회하는 기능을 구현 장바구니 조회 페이지에 전달할 DTO 클래스 생성

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    private Long cartItemId; //장바구니 상품 아이디
    private String itemNm; //상품명
    private int price; //상품 금액
    private int count; //수량

    private String imgUrl; //상품 이미지 경로

    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
    //장바구니 페이지에 전달할 데이터를 생성자의 파라미터로 만들어준다.
}
