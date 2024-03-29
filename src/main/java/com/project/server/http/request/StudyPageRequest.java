package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyPageRequest {
    @Schema(description = "제목" , example = "제목")
    private String title;
    @Schema(description = "내용" , example = "내용")
    private String contents;
    @Schema(description = "작성자" , example = "나")
    private String userId;
    @Schema(description = "하위 카테고리" , example = "하위 카테고리")
    private List<Long> subCategory;



//    @Schema(description = "최근 등록순" , example = "최근 등록순")
//    private String create;
//    @Schema(description = "지역" , example = "서울")
//    private String region;

}