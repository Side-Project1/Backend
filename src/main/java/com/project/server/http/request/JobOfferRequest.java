package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobOfferRequest {
    @Schema(description = "제목" , example = "제목")
    private String title;
    @Schema(description = "장소" , example = "장소")
    private String place;
    @Schema(description = "카테고리" , example = "카테고리")
    private String category;
    @Schema(description = "시작 날짜" , example = "2023-07-16")
    private LocalDate stDt;
    @Schema(description = "종료 날짜" , example = "2023-07-16")
    private LocalDate enDt;
    @Schema(description = "시작 시간" , example = "09:00")
    private String wst;
    @Schema(description = "종료 시간" , example = "18:00")
    private String wet;
    @Schema(description = "마감일" , example = "2023-07-16")
    private LocalDate deadLine;
    @Schema(description = "요구사항" , example = "의지")
    private String recruitment;
    @Schema(description = "담당 업무" , example = "담당 업무 이런거 있음")
    private String workContent;
    @Schema(description = "자격 요건" , example = "성실함")
    private String qualificationsNeeded;
    @Schema(description = "뽑는 인원" , example = "4")
    private Integer personNum;
    @Schema(description = "우대 사항" , example = "열심히")
    private String preferential;
    @Schema(description = "연락처" , example = "010-8921-8709")
    private String phone;
    @Schema(description = "이메일" , example = "dnd@naver.com")
    private String email;
    @Schema(description = "내용" , example = "내용")
    private String content;
    @Schema(description = "급여" , example = "1596원")
    private String salary;

}
