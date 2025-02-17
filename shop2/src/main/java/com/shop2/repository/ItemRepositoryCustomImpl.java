package com.shop2.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop2.constant.ItemSellStatus;
import com.shop2.dto.ItemSearchDto;
import com.shop2.entity.Item;
import com.shop2.entity.QItem;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

// ItemRepositoryCustom 인터페이스를 구현하는 ItemRepositoryCustomImpl 클래스를 작성 이때 주의할 점은 클래스 명 끝에 Impl을 붙여주어야 정상적으로 동작한다.
//QueryDsl에서 BooleanExpression이라는 where절에서 사용할 수 있는 값을 지원. BooleanExpression을 반환하는 메소드를 만들고 해당 조건들을 다른 쿼리를 생성할 때 사용할 수 있음
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        // "all"이 searchDateType type인지 검사
        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        }
        else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
       QueryResults<Item> results = queryFactory
               .selectFrom(QItem.item)
               .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                       searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                       searchByLike(itemSearchDto.getSearchBy(),
                               itemSearchDto.getSearchQuery()))
               .orderBy(QItem.item.id.desc())
               .offset(pageable.getOffset())
               .limit(pageable.getPageSize())
               .fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
