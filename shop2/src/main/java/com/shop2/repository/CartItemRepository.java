package com.shop2.repository;


import com.shop2.dto.CartDetailDto;
import com.shop2.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//장바구니에 들어갈 상품을 저장하거나 조회하기 위해서 CartItemRepository 인터페이스 생성
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회한다.
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.shop2.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}

