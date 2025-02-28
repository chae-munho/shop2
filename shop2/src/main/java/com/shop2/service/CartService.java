package com.shop2.service;

import com.shop2.dto.CartDetailDto;
import com.shop2.dto.CartItemDto;
import com.shop2.dto.CartOrderDto;
import com.shop2.dto.OrderDto;
import com.shop2.entity.Cart;
import com.shop2.entity.CartItem;
import com.shop2.entity.Item;
import com.shop2.entity.Member;
import com.shop2.repository.CartItemRepository;
import com.shop2.repository.CartRepository;
import com.shop2.repository.ItemRepository;
import com.shop2.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

//장바구니에 상품을 담는 로직을 작성하기 위해서 CartService 클래스 생성
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email) {

        //장바구니에 담을 상품 엔티티를 조회한다
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원 엔티티를 조회한다.
        Member member = memberRepository.findByEmail(email);

        //현재 로그인한 회원의 장바구니 엔티티를 조회한다.
        Cart cart = cartRepository.findByMemberId(member.getId());
        //상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티를 생성한다.
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        //현재 상품이 장바구니에 이미 들어가 있는지 조회한다.
        CartItem savedCartItem =
                cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        //장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼을 더해준다.
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            //장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용하여 CartItem 엔티티를 생성한다.
            CartItem cartItem =
                    CartItem.createCartItem(cart, item, cartItemDto.getCount());
            //장바구니에 들어갈 상품을 저장한다.
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }


    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<CartDetailDto>();
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) { //장바구니에 상품을 한 번도 안 담았을 경우 장바구니 엔티티가 없으므로 빈 리스트를 반환
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());  // 장바구니에 담겨있는 상품 정보를 조회
        return cartDetailDtoList;
    }

    // CartService 클래스에 장바구니 상품의 수량을 업데이트하는 로직 추가
    //자바스크립트 코드에서 업데이트할 장바구니 상품 번호는 조작이 가능하므로 현재 로그인한 회원과 해당 장바구니 상품을 저장한 회원이 같은지 검사하는 로직 작성
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }
    //장바구니 상품의 수량을 업데이트하는 메소드
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }
    //상품 정보에 있는 x 버튼을 클릭할 때 장바구니에 넣어 놓은 상품을 삭제하는 예제
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }
    //CartService 클래스에서는 주문 로직으로 전달할 orderDto 리스트 생성 및 주문 로직 호출, 주문한 상품은 장바구니에서 제거하는 로직을 구현
    // 장바구니에서 선택된 상품을 주문하는 기능을 수행
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }
        Long orderId = orderService.orders(orderDtoList, email);
        //장바구니에서 상품을 주문했으면 주문한 상품을 장바구니에서 삭제하는 반복문
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        //최종적으로 주문 ID를 반환한다.
        return orderId;
    }
}
