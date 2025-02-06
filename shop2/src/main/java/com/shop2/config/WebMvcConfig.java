package com.shop2.config;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer { // WebMvcConfigurer는 여러 mvc 관련 설정을 조정할 수 있도록 제공되는 인터페이스

      //application.properties에 설정한 uploadPath 프로퍼티 값을 읽어온다
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {  //addResourceHandlers() 메서드는 정적 리소스 (이미지, css, js 등)의 경로를 설정하기 위해 오버라이드 된다. ResourceHandlerRegistry 객체를 사용하여 정적 자원의 URL 매핑을 정의할 수 있다.
        registry.addResourceHandler("/images/**")  // /images/** 경로로 시작하는 모든 요청을 특정 로컬 경로와 매핑한다
                .addResourceLocations(uploadPath);  // addResourceLocations(uploadPath)는 uploadPath에 지정된 실제 파일 경로를 정적 리소스 디렉터리로 매핑한다.
    }

}
