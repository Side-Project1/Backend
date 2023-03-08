package com.project.server.repository.Study;


import com.project.server.entity.Study;

import com.project.server.repository.Study.StudyRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.project.server.entity.QStudy.study;


public class StudyRepositoryCustomImpl extends QuerydslRepositorySupport implements StudyRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public StudyRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Study.class);
        this.queryFactory=queryFactory;
    }

    @Override
    public Page<Study> findBySearchOption(Pageable pageable, String title, String contents) {
        JPQLQuery<Study> query =  queryFactory.selectFrom(study)
                .where(eqTitle(title), eqContents(contents));

        List<Study> studies = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Study>(studies, pageable, query.fetchCount());
    }

    private BooleanExpression eqTitle(String title) {
        if(title == null || title.isEmpty()) {
            return null;
        }
        return study.title.eq(title);
    }

//    private BooleanExpression containName(String name) {
//        if(name == null || name.isEmpty()) {
//            return null;
//        }
//        return study.name.containsIgnoreCase(name);
//    }

    private BooleanExpression eqContents(String contents) {
        if(contents == null || contents.isEmpty()) {
            return null;
        }
        return study.contents.eq(contents);

    }
}
