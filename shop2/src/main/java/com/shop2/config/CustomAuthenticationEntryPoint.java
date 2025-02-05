package com.shop2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
// 인증되지 않은 사용자가 리소스를 요청할 경우 Unauthorized 에러를 발생하도록 AuthenticationEntryPoint 인터페이스를 구현
//AutenticationEntryPoint : 인증이 안된 익명의 사용자가 인증이 필요한 인드포인트로 접근하게 된다면 Spring Security의 기본 설정으로 HttpStatus 401과 함께 스프링의 기본 오류페이지를 보여준다.
// HttpStatus 401 Unauthorized는 사용자가 인증되지 않았거나 유효한 인증 정보가 부족하여 요청이 거부된 것을 말한다.
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint  {  // spring security에서 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 실행되는 인터페이스
    //commence 메서드는 인증되지 않은 사용자가 요청을 보낼 때 호출된다.
    @Override
                        //HttpServletRequest request : 클라이언트의 HTTP 요청 객체, HttpServletResponse response : 서버가 클라이언트에게 응답을 보낼 때 사용하는 객체, AuthenticationException authException : 인증 관련 예외가 발생하면 해당 예외 객체가 전달된다.
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
