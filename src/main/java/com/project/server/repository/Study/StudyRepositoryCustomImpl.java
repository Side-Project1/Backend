package com.project.server.repository.Study;


import com.project.server.entity.Study;
import com.project.server.http.response.StudyPageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.project.server.entity.QJobCategory.jobCategory;
import static com.project.server.entity.QStudy.study;
import static com.project.server.entity.QUsers.users;


public class StudyRepositoryCustomImpl extends QuerydslRepositorySupport implements StudyRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public StudyRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Study.class);
        this.queryFactory = jpaQueryFactory;
    }


    @Override
    public List<StudyPageResponse> findPageStudy(Pageable pageable, String title, String contents, List<Long> subCategory) {

        return queryFactory.select(
                        Projections.bean(StudyPageResponse.class, study.id,study.title, study.contents, users.userId,study.region,study.createdDate))
                .from(study)
                .join(users).on(study.users.id.eq(users.id))
                .join(study.jobCategoryList, jobCategory)
                .where(likeTitle(title), likeContents(contents), jobCategory.id.in(subCategory))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(study.createdDate.desc())
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if(title == null || title.isEmpty()){
            return null;
        }
        return study.title.like(title);
    }

    private BooleanExpression likeContents(String contents) {
        if(contents == null || contents.isEmpty()){
            return null;
        }
        return study.contents.like(contents);
    }

    private BooleanExpression likeRegion(String region) {
        if(region == null || region.isEmpty()){
            return null;
        }
        return study.region.like(region);
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
