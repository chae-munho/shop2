package com.shop2.controller;


import com.shop2.dto.CartDetailDto;
import com.shop2.dto.CartItemDto;
import com.shop2.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.List;

//장바구니에와 관련된 요청들을 처리하기 위해서 CartController 클래스 생성
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {
        //장바구니에 담을 상품 정보를 받는 cartItemDTO 객체에 데이터 바인딩 시 에러가 있는지 검사한다.
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        //현재 로그인한 회원의 이메일 정보를 변수에 저장한다.
        String email = principal.getName();
        Long cartItemId;
        try {
            //화면으로부터 넘어온 장바구니에 담을 상품 정보와 현재 로그인한 회원의 이메일 정보를 이용하여 장바구니에 상품을 담는 로직을 호출한다.
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        //결과값으로 생성된 장바구니에 상품 아이디와 요청이 성공하였다는 HTTP 응답 상태 코드를 반환한다.
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 페이지로 이동할 수 있도록 CartController 클래스에 메소드 추가
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model) {
        List<CartDetailDto> cartDetailList =
                cartService.getCartList(principal.getName()); //현재 로그인한 사용자의 이메일 정보를 이용하여 장바구니에 담겨있는 상품 정보를 조회한다
        model.addAttribute("cartItems", cartDetailList);  //조회한 장바구니 상품 정보를 뷰로 전달한다.
        return "cart/cartList";
    }
    //장바구니 상품의 수량을 업데이트하는 요청을 처리하는 로직
    @PatchMapping(value = "/cartItem/{cartItemId}") //PatchMapping : Http patch 요청을 처리하는 매칭 어노테이션. patch는 리소스의 일부만 수정할 때 사용된다.
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestParam("count") int count, Principal principal) {
        //장바구니에 담겨있는 상품의 개수를 0개 이하로 업데이트 요청을 할 때 에러 메세지를 담아서 반환한다.
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
            //수정권한을 체크한다.
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        //장바구니 상품을 업데이트 한다.
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
    //장바구니 상품을 삭제하는 요청을 처리하는 컨트롤러 작성
    @DeleteMapping(value = "/cartItem/{cartItemId}") //HTTP 메소드에서 delete의 경우 요청된 자원을 삭제할 때 사용된다. 장바구니 상품을 삭제하기 때문에 deletemapping을 사용
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
        if (!cartService.validateCartItem(cartItemId, principal.getName())) {  //수정권한 확인
            return new ResponseEntity<String>("수정 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        //해당 장바구니 상품을 삭제
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
