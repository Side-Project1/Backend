package com.project.server.repository.comment;

import com.project.server.http.response.CommentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponse> findAllByPromotion(Pageable pageable, Long promotionId);
}
