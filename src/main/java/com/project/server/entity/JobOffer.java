package com.project.server.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer extends BaseBoard{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "job_offer_sn", columnDefinition = "BINARY")
    private Integer id;
    @Column
    private String title;
    @Column
    private String place;
    @Column
    private LocalDateTime stDt;     // 일하는 시작 날짜
    @Column
    private LocalDateTime enDt;     // 일 종료 날짜
    @Column
    private LocalDateTime wStT;    // 일 시작 시간
    @Column
    private LocalDateTime wEnT;     // 일 종료 시간
    @Column
    private LocalDateTime deadLine; // 공고 마감일
    @Column
    private boolean deadLineCheck; // 공고 마감 버튼 true 공고 마감, false 공고 중
    @Column
    private String recruitment;     // 모집 분야
    @Column
    private String workContent;     // 담당 업무
    @Column
    private String qualificationsNeeded; // 자격 요건
    @Column
    private Integer personNum;      // 모집 인원
    @Column
    private String preferential;    // 우대 사항
    @Column
    private String phone;           // 연락처
    @Column
    private String email;           // 이메일
    @Column
    private String content;         // 자유 내용
    @Column
    private String salary;          // 급여
}


//제목, 장소 (시) + 기타주소 or 도로명주소 , 기간 : (Type 여행예약) ,
//        근무 시간 : Type 시간 ,공고 마감일, 모집 분야, 담당 업무, 자격 요건
//        모집 인원, 우대 사항, 연락처 및 이메일, 자유 내용, 급여)
//        작성자, 조회수
