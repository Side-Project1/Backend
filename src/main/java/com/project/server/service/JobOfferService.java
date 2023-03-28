package com.project.server.service;

import com.project.server.entity.JobOffer;
import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.JobOfferPageRequest;
import com.project.server.http.request.JobOfferRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.JobOfferRepository;
import com.project.server.repository.JobOfferRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferRepositoryImpl jobImpl;


    public ResponseEntity creatJobOffer(User user, JobOfferRequest jobOfferRequest) {
        try {
            JobOffer jobOffer = JobOffer.builder()
                    .title(jobOfferRequest.getTitle())
                    .place(jobOfferRequest.getPlace())
                    .category(jobOfferRequest.getCategory())
                    .stDt(jobOfferRequest.getStDt())
                    .enDt(jobOfferRequest.getEnDt())
                    .wst(jobOfferRequest.getWst())
                    .wet(jobOfferRequest.getWet())
                    .deadLine(jobOfferRequest.getDeadLine())
                    .recruitment(jobOfferRequest.getRecruitment())
                    .workContent(jobOfferRequest.getWorkContent())
                    .qualificationsNeeded(jobOfferRequest.getQualificationsNeeded())
                    .personNum(jobOfferRequest.getPersonNum())
                    .preferential(jobOfferRequest.getPreferential())
                    .phone(jobOfferRequest.getPhone())
                    .email(jobOfferRequest.getEmail())
                    .content(jobOfferRequest.getContent())
                    .salary(jobOfferRequest.getSalary())
                    .user(user)
                    .build();
            jobOffer.setWrtrId(user.getUserId());
            jobOfferRepository.save(jobOffer);
            return new ResponseEntity(new ApiRes("구인글 생성 완료", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 생성 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getJobOffer(Integer id) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            jobOffer.setViewCount(jobOffer.getViewCount()+1);
            return new ResponseEntity(new ApiRes("구인글 검색 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 검색 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updateJobOffer(User user, Integer id, JobOfferRequest jobOfferRequest) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            if (jobOffer.getUser().getId() == user.getId()) {
                jobOffer.setTitle(jobOfferRequest.getTitle());
                jobOffer.setPlace(jobOfferRequest.getPlace());
                jobOffer.setStDt(jobOfferRequest.getStDt());
                jobOffer.setEnDt(jobOfferRequest.getEnDt());
                jobOffer.setWst(jobOfferRequest.getWst());
                jobOffer.setWet(jobOfferRequest.getWet());
                jobOffer.setDeadLine(jobOfferRequest.getDeadLine());
                jobOffer.setRecruitment(jobOfferRequest.getRecruitment());
                jobOffer.setWorkContent(jobOfferRequest.getWorkContent());
                jobOffer.setQualificationsNeeded(jobOfferRequest.getQualificationsNeeded());
                jobOffer.setPersonNum(jobOfferRequest.getPersonNum());
                jobOffer.setPreferential(jobOfferRequest.getPreferential());
                jobOffer.setPhone(jobOfferRequest.getPhone());
                jobOffer.setEmail(jobOfferRequest.getEmail());
                jobOffer.setContent(jobOfferRequest.getContent());
                jobOffer.setSalary(jobOfferRequest.getSalary());
            } else {

            }

            return new ResponseEntity(new ApiRes("구인글 업데이트 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 업데이트 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deleteJobOffer(User user, Integer id) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            if (jobOffer.getUser().getId() == user.getId()) {
                jobOfferRepository.deleteById(jobOffer.getId());
            }
            return new ResponseEntity(new ApiRes("구인글 삭제 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deadLineCheck(User user, Integer id) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            if (jobOffer.getUser().getId() == user.getId()) {
                jobOffer.setDeadLineCheck(true);
            }
            return new ResponseEntity(new ApiRes("구인글 삭제 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 삭제 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getJobOfferList(Pageable pageable, JobOfferPageRequest jobOfferPageRequest, String category) {
        try {
//        같은 내용
//        ArrayList<String> categoryList = (category != null) ? new ArrayList<>(Arrays.asList(category.split(","))) : new ArrayList<>();
            ArrayList<String> categoryList = new ArrayList<>();
            if (category != null) {
                for (String datum : category.split(",")) {
                    categoryList.add(datum);
                }
            }
            Page<JobOffer> jobOfferPage = jobImpl.findJobOfferList(pageable, jobOfferPageRequest, categoryList);
            return new ResponseEntity(new ApiRes("구인글 목록 조회 완료", HttpStatus.OK, jobOfferPage), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 목록 조회 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        
    }
}
