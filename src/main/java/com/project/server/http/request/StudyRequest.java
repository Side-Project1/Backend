package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.EnumStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyRequest {
    @Schema(description = "제목" , example = "스터디 모집")
    private String title; //제목
    @Schema(description = "인원 수" , example = "4")
    private int max; //인원 수

    @Schema(description = "지역" , example = "서울")
    private String region; //지역


    @Schema(description = "내용" , example = "스터디 모집합니다")
    private String contents; // 자유 양식

    @Schema(description = "모집 여부" , example = "Y")
    private EnumStatus.Status recruitment;
    @Schema(description = "하위 카테고리" , example = "1, 2")
    private List<Long> subCategory;

}
