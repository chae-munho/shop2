package com.shop2.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
//AuditorAwareImpl 인터페이스는 spring data jpa에서 엔티티의 생성자(createdBy) 및 수정자(lastMondifieldBy)를 자동으로 기록할 때 사용  <String>는 반환할 감시자(auditor)의 데이터 타입이 String임을 의미한다.
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() { //getCurrentAuditor()메서드는 현재 인증된 사용자의 정보를 가져옴. Optional<String>이므로 null값이 올 가능성이 있을 때 이를 안전하게 처리하기 위해 Optional을 사용
        Authentication authentication =  // 현재 로그인한 사용자의 인증 정보(Authentication)을 가져오는 코드
                SecurityContextHolder.getContext().getAuthentication();  // SecurityContextHolder.getContext()을 호출하면 보안 컨텍스트가(SecurityContext) 반환. SecurityContext.getAuthentication()을 호출하면 현재 인증된 사용자의 Authentication(인증정보)을 가져옴.
        // 로그인한 사용자가 없으면 authentication에 null을 반환 받을 수 있다.
        String userId = ""; //기본적으로 userId를 빈 문자열로 초기화. 사용자가 로그인하지 않은 경우를 대비한 기본 값
        if (authentication != null) { //로그인 사용자가 있으면 userId에는 로그인한 사용자의 이름 또는 ID를 가져옴 Spring Security에서 기본적으로 getName()은 사용자명을 반환하지만, 커스텀 인증 방식에서는 이메일이나 사용자의 ID를 반환할 수 있음 해당 프로젝트에서는 이메일
            userId = authentication.getName();   // 현재 로그인 한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정한다.
        }
        return Optional.of(userId);  //userId를 Optional.of(userId)로 감싸서 반환. 이렇게 하면 null이 반환되지 않고 Optional.empty()를 사용하는 것보다 빈문자열을 반환하는 방식이 선택된다..
    }
}
