package com.shop2.repository;

import com.shop2.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //현재 로그인한 회원의 Cart 엔티티를 찾기 위해서 CartRepository에 쿼리 메소드 추가
    Cart findByMemberId(Long memberId);

}
