package com.shop2.config;

import com.shop2.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Autowired
    private MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository()))  //세션이 없을때도 csrf가 정상적으로 동작하게 하려면 SecurityConfig에 CookieCsrfTokenRepository를 사용해야함 해당 방식은 서버에서 첫 번째 요청이더라도 http only 쿠키를 생성해서 csrf 토큰을 생성 후 저장한다.
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer  //시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()  //permitAll()을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정한다. 메인 페이지, 회원 관련 url, 뒤에서 만들 상품 상세 페이지, 상품 이미지를 불러오는 경로가 이에 해당한다
                        .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest()  // 그 이외의 요청들은
                        .authenticated()  // 인증을 요구한다
                ).formLogin(formLoginCustomizer -> formLoginCustomizer
                        .loginPage("/members/login")
                        //SecurityConfig에 redirect할 때 항상 "/" 경로로 이동할 수 있도록 true 옵션 설정.
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("email")
                        .failureHandler(new FormLoginAuthenticationFailureHandler())
                ).logout( logoutCustomizer -> logoutCustomizer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                ).exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())  // 인증되지 않은 사용자가 리소스에 접근하였을 떄 수행되는 핸들러를 등록한다.
                )
                .build()
                ;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
