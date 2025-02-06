package com.shop2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//cart 테이블은 member_id 컬럼을 외래키로 갖는다. member_id를 foreign key로 지정하는 쿼리문이 실행됨
@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity{
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //즉시로딩
    @OneToOne(fetch = FetchType.LAZY)  //one to one 어노테이션을 이용해 회원 엔티티와 일대일로 매핑을 함
    @JoinColumn(name = "member_id")  // JoinColumn을 이용해 매핑할 외래키를 지정한다. name 속성에는 매핑할 외래키의 이름을 설정
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
