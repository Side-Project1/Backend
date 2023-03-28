package com.project.server.repository.comment;

import com.project.server.entity.Comment;
import com.project.server.entity.Promotion;
import com.project.server.entity.Study;
import com.project.server.http.response.CommentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.project.server.entity.QComment.comment;

public class CommentRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Comment.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public List<CommentResponse> findAllByPromotion(Pageable pageable, Long promotionId) {
        return queryFactory.select(
                        Projections.bean(CommentResponse.class, comment.id.as("commentId"), comment.promotion.id.as("promotionId"), comment.user.userId, comment.comments,
                                comment.ref, comment.refStep, comment.parent.id.as("parentId"), comment.childCount,
                                comment.isDeleted, comment.isPrivated, comment.createdDate))
                .from(comment)
                .where(comment.promotion.id.eq(promotionId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.ref.asc(), comment.refStep.asc())
                .fetch();
    }

//    @Override
//    public List<Comment> findAllByStudy(Study study) {
//        return queryFactory.selectFrom(comment)
//                .leftJoin(comment.parent)
//                .fetchJoin()
//                .where(comment.study.id.eq(study.getId()))
//                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdDate.asc())
//                .fetch();
//    }




}
