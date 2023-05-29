package com.project.server.service;

import com.project.server.entity.JobCategory;
import com.project.server.entity.Promotions;
import com.project.server.entity.Users;
import com.project.server.http.request.PromotionRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.http.response.PromotionPageResponse;
import com.project.server.http.response.PromotionResponse;
import com.project.server.repository.UsersRepository;
import com.project.server.repository.promotion.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final UsersRepository usersRepository;
    private final PromotionRepository promotionRepository;

    @Transactional
    public ResponseEntity getPagePromotion(Pageable pageable, String title, String contents, List<Long> subCategory) {
        try {
            List<PromotionPageResponse> promotionPageResponseList = promotionRepository.findPagePromotion(pageable, title, contents, subCategory);

            promotionPageResponseList.forEach(data -> data.setJobCategoryList(promotionRepository.findById(data.getId()).get().getJobCategoryList()));
            return new ResponseEntity(new ApiRes("홍보글 페이지 조회 성공", HttpStatus.OK, promotionPageResponseList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 페이지 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getPromotion(Long id) {
        try {
            Promotions promotions = promotionRepository.findById(id).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            PromotionResponse promotionResponse = new PromotionResponse(promotions);
            return new ResponseEntity(new ApiRes("홍보글 조회 성공", HttpStatus.OK, promotionResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity createPromotion(Users users, PromotionRequest promotionRequest) {
        try {
            Promotions promotions = Promotions.builder()
                    .title(promotionRequest.getTitle())
                    .contents(promotionRequest.getContents())
                    .users(users)
                    .build();
            promotionRequest.getSubCategory().forEach(id -> promotions.getJobCategoryList().add(new JobCategory(id)));
            promotionRepository.save(promotions);
            Users saveUsers = usersRepository.findById(users.getId()).get();
            saveUsers.getPromotionsList().add(promotions);
            return new ResponseEntity(new ApiRes("홍보글 생성 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 생성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updatePromotion(Users users, Long id, PromotionRequest promotionRequest) {
        try {
            Promotions promotions = promotionRepository.findById(id).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            if(!users.getUserId().equals(promotions.getUsers().getUserId())) throw new IllegalStateException("글쓴이만 수정할 수 있습니다.");

            promotions.setTitle(promotionRequest.getTitle());
            promotions.setContents(promotionRequest.getContents());
            List<JobCategory> jobCategoryList = new ArrayList<>();
            promotionRequest.getSubCategory().forEach(jobCategoryId -> jobCategoryList.add(new JobCategory(jobCategoryId)));
            promotions.setJobCategoryList(jobCategoryList);

            return new ResponseEntity(new ApiRes("홍보글 수정 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 수정 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity deletePromotion(Users users, Long id) {
        try {
            Promotions promotions = promotionRepository.findById(id).orElseThrow(()->new IllegalStateException("게시글이 존재하지 않습니다"));
            if(!users.getUserId().equals(promotions.getUsers().getUserId())) throw new IllegalStateException("글쓴이만 삭제할 수 있습니다.");

            promotionRepository.deleteById(id);

            return new ResponseEntity(new ApiRes("홍보글 삭제 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("홍보글 삭제+ 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
