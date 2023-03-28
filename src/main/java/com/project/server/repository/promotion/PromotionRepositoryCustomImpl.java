package com.project.server.repository.promotion;

import com.project.server.entity.Promotion;
import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.response.PromotionPageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.project.server.entity.QPromotion.promotion;
import static com.project.server.entity.QUser.user;

public class PromotionRepositoryCustomImpl extends QuerydslRepositorySupport implements PromotionRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public PromotionRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Promotion.class);
        this.queryFactory = jpaQueryFactory;
    }


    @Override
    public List<PromotionPageResponse> findPagePromotion(Pageable pageable, PromotionPageRequest pr) {
        return queryFactory.select(
                Projections.bean(PromotionPageResponse.class, promotion.title, user.userId, promotion.createdDate))
                        .from(promotion)
                .join(user).on(promotion.user.id.eq(user.id))
                .where(likeTitle(pr.getTitle()), likeContents(pr.getContents()), eqUserId(pr.getUserId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(promotion.createdDate.desc())
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if(title == null || title.isEmpty()){
            return null;
        }
        return promotion.title.like(title);
    }

    private BooleanExpression likeContents(String contents) {
        if(contents == null || contents.isEmpty()){
            return null;
        }
        return promotion.contents.like(contents);
    }

    private BooleanExpression eqUserId(String userId) {
        if(userId == null || userId.isEmpty()){
            return null;
        }
        return user.userId.eq(userId);
    }
}
