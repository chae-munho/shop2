package com.shop2.repository;

import com.shop2.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Query 어노테이션을 이용하여 주문 이력을 조회하는 쿼리 작성.
//Pageable은 페이징 및 정렬 기능을 처리하기 위한 인터페이스. 대량의 데이터를 한 번에 모두 가져오는 대신, 데이터를 페이지 단위로 나눠서 가져오는 기능을 제공 주로 페이징 처리와 정렬 기능에 사용되며 클라이언트로부터 받은 페이지 번호, 크기, 정렬 조건등을 기반으로 데이터를 조회
public interface OrderRepository extends JpaRepository<Order, Long> {

    //현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞추서 조회
    @Query("select o from Order o " +
        "where o.member.email = :email " +
        "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    //현재 로그인한 회원의 주문 개수가 몇 개인지 조회
    @Query("select count(o) from Order o " +
        "where o.member.email = :email")
    Long countOrder(@Param("email") String email);

}
