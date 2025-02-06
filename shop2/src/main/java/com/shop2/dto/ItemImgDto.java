package com.shop2.dto;

import com.shop2.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;


// 상품 저장 후 상품 이미지에 대한 데이터를 전달한 DTO 클래스
@Getter
@Setter
public class ItemImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();  // 멤버 변수로 ModelMapper 객체를 추가한다

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);  // ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때, ItemImgDto로 값을 복사해서 반환. static 매소드로 선언해 ItemImgDto 객체를 생성하지 않아도 호출할 수 있도록 함.
    }


}
