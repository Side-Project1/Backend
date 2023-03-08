package com.project.server.repository;

import com.project.server.entity.JobOffer;
import com.project.server.http.request.JobOfferPageRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import static com.project.server.entity.QJobOffer.jobOffer;

@Component
public class JobOfferRepositoryImpl extends QuerydslRepositorySupport {
    @Autowired
    private JPAQueryFactory queryFactory;

    public JobOfferRepositoryImpl() {
        super(JobOffer.class);
    }

    public Page<JobOffer> findJobOfferList(Pageable pageable, JobOfferPageRequest jobOfferPageRequest, ArrayList<String> category) {
        JPQLQuery<JobOffer> query = queryFactory.selectFrom(jobOffer)
                .where(containsTitle(jobOfferPageRequest.getTitle()))
                .where(containsPlace(jobOfferPageRequest.getPlace()))
                .where(containsContent(jobOfferPageRequest.getContent()));

        if(!category.isEmpty()) query.where(jobOffer.category.in(category));

        this.getQuerydsl().applyPagination(pageable, query);
        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }

    private BooleanExpression containsTitle(String title) {
        if(title == null || title.isEmpty()) {
            return null;
        }
        return jobOffer.title.contains(title);
    }

    private BooleanExpression containsPlace(String place) {
        if(place == null || place.isEmpty()) {
            return null;
        }
        return jobOffer.place.contains(place);
    }

    private BooleanExpression containsContent(String content) {
        if(content == null || content.isEmpty()) {
            return null;
        }
        return jobOffer.content.contains(content);
    }
}
