package com.shop2.dto;


//상품 상세 페이지에서 주문할 상품의 아이디와 주문 수량을 전달받을 OrderDto 클래스 작성
//주문 로직을 작성하기 위해 OrderService 클래스 만듬

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count;
}
