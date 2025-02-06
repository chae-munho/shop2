package com.shop2.repository;

import com.shop2.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
//상품의 이미지 정보를 저장하기 위해서 repository 패키지 아래에 JpaRepository를 상속받는 ItenImgRepository 인터페이스 작성
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {


}
