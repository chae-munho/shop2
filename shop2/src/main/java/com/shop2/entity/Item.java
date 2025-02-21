package com.shop2.entity;

import com.shop2.constant.ItemSellStatus;
import com.shop2.dto.ItemFormDto;
import com.shop2.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@ToString
@Getter
@Setter
public class Item extends BaseEntity{
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    //private LocalDateTime regTime;

    //private LocalDateTime updateTime;

    //상품을 업데이트하는 로직을 구현. Item 클래스에 상품 데이터를 업데이트하는 로직 작성. 엔티티 클래스에 비즈니스 로직을 추가한다면 조금 더 객체지향적으로 코딩할 수 있고 코드를 재활용 할 수 있음.
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        //상품의 재고 수량에서 주문 후 남은 재고 수량을 구합니다.
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            //상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생시킵니다.
            throw  new OutOfStockException("상품의 재고가 부족합니다. (현재 " +
                    "재고 수량 :  " + this.stockNumber + ")");
        }
        //주문 후 남은 재고 수량을 상품의 현재 재고값으로 할당한다
        this.stockNumber = restStock;

    }


}
