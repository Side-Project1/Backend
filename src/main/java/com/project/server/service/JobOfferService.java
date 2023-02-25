package com.project.server.service;

import com.project.server.entity.JobOffer;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.JobOfferRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;

    public ResponseEntity creatJobOffer(JobOfferRequest jobOfferRequest) {
        try {
            JobOffer jobOffer = JobOffer.builder()
                    .title(jobOfferRequest.getTitle())
                    .place(jobOfferRequest.getPlace())
                    .stDt(jobOfferRequest.getStDt())
                    .enDt(jobOfferRequest.getEnDt())
                    .wStT(jobOfferRequest.getWStT())
                    .wEnT(jobOfferRequest.getWEnT())
                    .deadLine(jobOfferRequest.getDeadLine())
                    .deadLineCheck(false)
                    .recruitment(jobOfferRequest.getRecruitment())
                    .workContent(jobOfferRequest.getWorkContent())
                    .qualificationsNeeded(jobOfferRequest.getQualificationsNeeded())
                    .personNum(jobOfferRequest.getPersonNum())
                    .preferential(jobOfferRequest.getPreferential())
                    .phone(jobOfferRequest.getPhone())
                    .email(jobOfferRequest.getEmail())
                    .content(jobOfferRequest.getContent())
                    .salary(jobOfferRequest.getSalary())
                    .build();
            jobOfferRepository.save(jobOffer);
            return new ResponseEntity(new ApiRes("구인글 생성 완료", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 생성 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getJobOffer(Integer id) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            return new ResponseEntity(new ApiRes("구인글 검색 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 검색 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity updateJobOffer(Integer id, JobOfferRequest jobOfferRequest) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            jobOffer.setTitle(jobOfferRequest.getTitle());
            jobOffer.setPlace(jobOfferRequest.getPlace());
            jobOffer.setStDt(jobOfferRequest.getStDt());
            jobOffer.setEnDt(jobOfferRequest.getEnDt());
            jobOffer.setWStT(jobOfferRequest.getWStT());
            jobOffer.setWEnT(jobOfferRequest.getWEnT());
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
            jobOffer.setViewCount(jobOffer.getViewCount()+1);
            return new ResponseEntity(new ApiRes("구인글 삭제 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 삭제 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deleteJobOffer(Integer id) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            jobOfferRepository.deleteById(jobOffer.getId());
            return new ResponseEntity(new ApiRes("구인글 삭제 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 삭제 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deadLineCheck(Integer id) {
        try {
            JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", id));
            jobOffer.setDeadLineCheck(true);
            return new ResponseEntity(new ApiRes("구인글 삭제 완료", HttpStatus.OK, jobOffer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes("구인글 삭제 실패", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
