package com.project.server.repository.comment;

import com.project.server.entity.Comment;
import com.project.server.http.response.CommentResponse;
import com.project.server.http.response.StudyCommentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.project.server.entity.QComment.comment;
import static com.project.server.entity.QStudyComment.studyComment;

public class CommentRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Comment.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public List<CommentResponse> findAllByPromotion(Pageable pageable, Long promotionId) {
        return queryFactory.select(
                        Projections.bean(CommentResponse.class, comment.id.as("commentId"), comment.promotions.id.as("promotionId"), comment.users.userId, comment.comments,
                                comment.ref, comment.refStep, comment.parent.id.as("parentId"), comment.childCount,
                                comment.isDeleted, comment.isPrivated, comment.createdDate))
                .from(comment)
                .where(comment.promotions.id.eq(promotionId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.ref.asc(), comment.refStep.asc())
                .fetch();
    }



        @Override
    public List<StudyCommentResponse> findAllByStudy(Pageable pageable, Long studyId) {
        return queryFactory.select(
                        Projections.bean(StudyCommentResponse.class, studyComment.id.as("commentId"), studyComment.study.id.as("studyId"), studyComment.users.userId, studyComment.comments,
                                studyComment.ref, studyComment.refStep, studyComment.parent.id.as("parentId"), studyComment.childCount,
                                studyComment.isDeleted, studyComment.isPrivated, studyComment.createdDate))
                .from(studyComment)
                .where(studyComment.study.id.eq(studyId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(studyComment.ref.asc(), studyComment.refStep.asc())
                .fetch();
    }




}
