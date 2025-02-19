package com.shop2.dto;

//메인 페이지에서 상품을 보여줄 때 사용할 MainItemDto

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String itemDetail;

    private String imgUrl;
    private Integer price;

    @QueryProjection //생성자에 @QueryProjection 어노테이션을 선언하여 Qeurydsl로 결과 조회 시 MainItemDto 객체로 바로 받아오도록 활용
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
