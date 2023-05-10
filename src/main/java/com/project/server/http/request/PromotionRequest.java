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
public class PromotionRequest {
    @Schema(description = "제목" , example = "제목")
    private String title;
    @Schema(description = "내용" , example = "내용")
    private String contents;
    @Schema(description = "하위 카테고리" , example = "1, 2")
    private List<Long> subCategory;
}
