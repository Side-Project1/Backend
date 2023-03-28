package com.project.server.repository.promotion;

import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.response.PromotionPageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PromotionRepositoryCustom {
    List<PromotionPageResponse> findPagePromotion(Pageable pageable, PromotionPageRequest promotionPageRequest);
}
