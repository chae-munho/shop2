package com.shop2.service;


import com.shop2.dto.OrderDto;
import com.shop2.entity.Item;
import com.shop2.entity.Member;
import com.shop2.entity.Order;
import com.shop2.entity.OrderItem;
import com.shop2.repository.ItemRepository;
import com.shop2.repository.MemberRepository;
import com.shop2.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
