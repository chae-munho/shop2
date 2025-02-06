package com.shop2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class}) // 해당 클래스에 Auditing 기능을 포함
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {
    @CreatedBy  // entity가 생성되어 저장될 떄 시간이 자동 저장
    @Column(updatable = false)
    private String createdBy;  // 생성자

    @LastModifiedBy  // 조회한 Entity의 값을 변경할 때 시간이 자동 저장
    private String modifiedBy;  // 수정자
}
