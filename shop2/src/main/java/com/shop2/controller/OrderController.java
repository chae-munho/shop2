package com.shop2.controller;

import com.shop2.dto.OrderDto;
import com.shop2.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /*
        스프링에서 비동기 처리를 할 때 @RequestBody와 @ResponseBody 어노테이션을 사용
        1. @RequestBody : Http 요청의 본문 body에 담긴 내용을 자바 객체로 전달
        2. @ResponseBody : 자바 객체를 HTTP 요청의 body로 전달
     */

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto
            , BindingResult bindingResult, Principal principal){

        // 주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사
        if(bindingResult.hasErrors()){
            // 에러 메세지를 효율적으로 조합하기 위해 StringBuilder 객체를 생성
            StringBuilder sb = new StringBuilder();

            //유효성 검사에서 발생한 모든 필드 에러들을 가져온다
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            //모든 에러 메시지를 순회하면서 각 에러의 기본 메시지를 StringBuilder에 추가
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            //에러 메시지를 문자열로 반환하며 http 상태 코드는 400 bad request로 설정해 클라이언트에게 잘못된 요청임을 알립니다.
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        try {
            //화면으로 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출
            orderId = orderService.order(orderDto, email);
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        //결과값으로 생성된 주문 번호와 요청이 성공했다는 HTTP 응답 상태 코드를 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
