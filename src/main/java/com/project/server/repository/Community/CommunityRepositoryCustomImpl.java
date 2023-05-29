package com.project.server.repository.Community;

import com.project.server.entity.Community;
import com.project.server.http.response.CommunityPageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.project.server.entity.QCommunity.community;
import static com.project.server.entity.QUsers.users;



public class CommunityRepositoryCustomImpl extends QuerydslRepositorySupport implements CommunityRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public CommunityRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Community.class);
        this.queryFactory = jpaQueryFactory;
    }


    @Override
    public List<CommunityPageResponse> findPageCommunity(Pageable pageable, String title, String contents) {

        return queryFactory.select(
                        Projections.bean(CommunityPageResponse.class, community.id, community.title, community.contents, users.userId, community.createdDate))
                .from(community)
                .join(users).on(community.users.id.eq(users.id))
                .where(likeTitle(title), likeContents(contents))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(community.createdDate.desc())
                .fetch();
    }

    private BooleanExpression likeTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return community.title.like(title);
    }

    private BooleanExpression likeContents(String contents) {
        if (contents == null || contents.isEmpty()) {
            return null;
        }
        return community.contents.like(contents);
    }


    private BooleanExpression eqUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        return users.userId.eq(userId);
    }
}





