package com.project.server.repository.promotion;

import com.project.server.entity.Promotions;
import com.project.server.http.response.PromotionPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotions, Long>, PromotionRepositoryCustom {
    List<PromotionPageResponse> findPagePromotion(Pageable pageable, String title, String contents, List<Long> subCategory);
}
