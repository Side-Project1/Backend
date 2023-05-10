package com.project.server.repository.comment;

import com.project.server.http.response.CommentResponse;
import com.project.server.http.response.StudyCommentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponse> findAllByPromotion(Pageable pageable, Long promotionId);
    List<StudyCommentResponse> findAllByStudy(Pageable pageable, Long studyId);
}
