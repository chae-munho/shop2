package com.shop2.service;


import com.shop2.dto.OrderDto;
import com.shop2.dto.OrderHistDto;
import com.shop2.dto.OrderItemDto;
import com.shop2.entity.*;
import com.shop2.repository.ItemImgRepository;
import com.shop2.repository.ItemRepository;
import com.shop2.repository.MemberRepository;
import com.shop2.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

// 주문 로직을 작성하기 위한 service
@Service
//주문 처리 중 문제가 발생하면 모든 DB 작업이 롤백되어 데이터의 무결성이 유지된다.
@Transactional
//final로 선언된 필드에 대해 생성자 주입을 자동으로 생성 즉 의존성 주입을 위한 생성자를 직접 작성하지 않아도 된다
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        //주문 항목을 저장할 리스트를 생성, 한 번에 여러 상품을 주문할 수 있도록 리스트 형태로 주문 항목을 관리
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();

        //주문할 상품과 수량 정보를 이용해 새로운 OrderItem 객체를 생성
        OrderItem orderItem =
                OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);   //생성한 주문 엔티티를 저장
        //저장된 주문 객체의 ID값을 반환한다.
        return order.getId();

        //주문 관련 요청들을 처리하기 위해서 OrderController 클래스를 만듬. 상품 주문에서 웹 페이지의 새로 고침 없이 서버에 주문을 요청하기 위해서 비동기 방식을 사용
    }
    //OrderService 클래스에 주문 목록을 조회하는 로직을 구현
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        //유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);

        //유저의 주문 총 개수를 구합니다.
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        //주문 리스트를 순회하면서 구매 이력 페이지에 전달한 DTO를 생성
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                //주문한 상품의 대표 이미지를 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        //페이지 구현 객체를 생성하여 반환한다.
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    //주문을 취소하는 로직 구현
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {  //현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사를 함. 같을 때는 true를 반환하고 같지 않을 경우 fales 반환
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        //주문 취소 상태로 변경하면 변경 감지 기능에 의해서 트랜잭션이 끝날 때 update 쿼리가 실행된다.
        order.cancelOrder();
    }
}
