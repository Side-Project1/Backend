package com.project.server.service;

import com.project.server.entity.Promotion;
import com.project.server.entity.User;
import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.request.PromotionRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.PromotionPageResponse;
import com.project.server.http.response.PromotionResponse;
import com.project.server.repository.promotion.PromotionRepository;
import com.project.server.repository.promotion.PromotionRepositoryCustomImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionRepositoryCustomImpl promotionRepositoryCustom;

    public ResponseEntity getPagePromotion(Pageable pageable, PromotionPageRequest promotionPageRequest) {
        try {
            List<PromotionPageResponse> promotionPageResponseList = promotionRepositoryCustom.findPagePromotion(pageable, promotionPageRequest);
            return new ResponseEntity(new ApiRes("홍보글 페이지 조회 성공", HttpStatus.OK, promotionPageResponseList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 페이지 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getPromotion(Long id) {
        try {
            Promotion promotion = promotionRepository.findById(id).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            PromotionResponse promotionResponse = PromotionResponse.builder()
                    .title(promotion.getTitle())
                    .contents(promotion.getContents())
                    .writer(promotion.getUser().getUserId())
                    .createdDate(promotion.getCreatedDate())
                    .updateDate(promotion.getUpdatedDate())
                    .build();
            return new ResponseEntity(new ApiRes("홍보글 조회 성공", HttpStatus.OK, promotionResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity createPromotion(User user, PromotionRequest promotionRequest) {
        try {
            Promotion promotion = Promotion.builder()
                    .title(promotionRequest.getTitle())
                    .contents(promotionRequest.getContents())
                    .user(user)
                    .build();
            promotionRepository.save(promotion);
            return new ResponseEntity(new ApiRes("홍보글 생성 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 생성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updatePromotion(User user, Long id, PromotionRequest promotionRequest) {
        try {
            Promotion promotion = promotionRepository.findById(id).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            if(!user.getUserId().equals(promotion.getUser().getUserId())) throw new IllegalStateException("글쓴이만 수정할 수 있습니다.");

            promotion.setTitle(promotionRequest.getTitle());
            promotion.setContents(promotionRequest.getContents());

            return new ResponseEntity(new ApiRes("홍보글 수정 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity deletePromotion(User user, Long id) {
        try {
            Promotion promotion = promotionRepository.findById(id).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            if(!user.getUserId().equals(promotion.getUser().getUserId())) throw new IllegalStateException("글쓴이만 삭제할 수 있습니다.");
            
            promotionRepository.deleteById(id);
            
            return new ResponseEntity(new ApiRes("홍보글 삭제 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 삭제+ 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
