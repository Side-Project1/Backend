package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.JobOffer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobOfferResponse {
    private Integer jobOfferId;
    private String title;
    private String category;        // 카테고리
    private String place;           // 지역
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate stDt;     // 일하는 시작 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate enDt;     // 일 종료 날짜
    private String wst;    // 일 시작 시간
    private String wet;     // 일 종료 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadLine; // 공고 마감일
    private boolean deadLineCheck; // 공고 마감 버튼 true 공고 마감, false 공고 중
    private String recruitment;     // 모집 분야
    private String workContent;     // 담당 업무
    private String qualificationsNeeded; // 자격 요건
    private Integer personNum;      // 모집 인원
    private String preferential;    // 우대 사항
    private String phone;           // 연락처
    private String email;           // 이메일
    private String content;         // 자유 내용
    private String salary;          // 급여
    private String UserId;

//    public JobOfferResponse(JobOffer jobOffer) {
//        this.jobOfferId = jobOffer.getId();
//        this.title = jobOffer.getTitle();
//        this.category = jobOffer.getCategory();
//        this.place = place;
//        this.stDt = stDt;
//        this.enDt = enDt;
//        this.wst = wst;
//        this.wet = wet;
//        this.deadLine = deadLine;
//        this.deadLineCheck = deadLineCheck;
//        this.recruitment = recruitment;
//        this.workContent = workContent;
//        this.qualificationsNeeded = qualificationsNeeded;
//        this.personNum = personNum;
//        this.preferential = preferential;
//        this.phone = phone;
//        this.email = email;
//        this.content = content;
//        this.salary = salary;
//        UserId = userId;
//    }
}
