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
import static com.project.server.entity.QUsers.users;

public class PromotionRepositoryCustomImpl extends QuerydslRepositorySupport implements PromotionRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public PromotionRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Promotions.class);
        this.queryFactory = jpaQueryFactory;
    }

    public List<PromotionPageResponse> findPagePromotion(Pageable pageable, String title, String contents, List<Long> subCategory) {
        return queryFactory.selectDistinct(
                Projections.bean(PromotionPageResponse.class, promotions.id, promotions.title, users.userId, promotions.createdDate))
                        .from(promotions)
                .join(users).on(promotions.users.id.eq(users.id))
                .join(promotions.jobCategoryList, jobCategory)
                .where(likeTitle(title), likeContents(contents), inSubCategory(subCategory))
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

    private BooleanExpression inSubCategory(List<Long> subCategory) {
        if(subCategory == null || subCategory.isEmpty()){
            return null;
        }
        return jobCategory.id.in(subCategory);
    }

    private BooleanExpression eqUserId(String userId) {
        if(userId == null || userId.isEmpty()){
            return null;
        }
        return users.userId.eq(userId);
    }

    private BooleanExpression eqJobCategory(String subCategory) {
        if(subCategory == null || subCategory.isEmpty()){
            return null;
        }
        return users.userId.eq(subCategory);
    }
}
