package com.project.server.repository.promotion;

import com.project.server.entity.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotions, Long>, PromotionRepositoryCustom {
}
