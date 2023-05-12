package com.project.server.repository.promotion;

import com.project.server.entity.Promotions;
import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.response.PromotionPageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


import static com.project.server.entity.QJobCategory.jobCategory;
import static com.project.server.entity.QPromotions.promotions;
import static com.project.server.entity.QUser.user;

public class PromotionRepositoryCustomImpl extends QuerydslRepositorySupport implements PromotionRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public PromotionRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Promotions.class);
        this.queryFactory = jpaQueryFactory;
    }


    @Override
    public List<PromotionPageResponse> findPagePromotion(Pageable pageable, PromotionPageRequest pr) {
        return queryFactory.selectDistinct(
                Projections.bean(PromotionPageResponse.class, promotions.id, promotions.title, user.userId, promotions.createdDate))
                        .from(promotions)
                .join(user).on(promotions.user.id.eq(user.id))
                .join(promotions.jobCategoryList, jobCategory)
                .where(likeTitle(pr.getTitle()), likeContents(pr.getContents()), jobCategory.id.in(pr.getSubCategory()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(promotions.createdDate.desc())
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if(title == null || title.isEmpty()){
            return null;
        }
        return promotions.title.contains(title);
    }

    private BooleanExpression likeContents(String contents) {
        if(contents == null || contents.isEmpty()){
            return null;
        }
        return promotions.contents.contains(contents);
    }

    private BooleanExpression eqUserId(String userId) {
        if(userId == null || userId.isEmpty()){
            return null;
        }
        return user.userId.eq(userId);
    }

    private BooleanExpression eqJobCategory(String subCategory) {
        if(subCategory == null || subCategory.isEmpty()){
            return null;
        }
        return user.userId.eq(subCategory);
    }
}
