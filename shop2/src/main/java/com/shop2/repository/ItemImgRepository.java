package com.shop2.repository;

import com.shop2.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//상품의 이미지 정보를 저장하기 위해서 repository 패키지 아래에 JpaRepository를 상속받는 ItenImgRepository 인터페이스 작성
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    //매개변수로 넘겨준 상품 아이디를 가지며 상품 이미지 아이디의 오름차순으로 가져오는 쿼리 메소드
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}
