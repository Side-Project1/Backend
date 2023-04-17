package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobOfferPageRequest {
    @Schema(description = "제목" , example = "제목")
    private String title;
    @Schema(description = "장소" , example = "장소")
    private String place;
    @Schema(description = "내용" , example = "내용")
    private String content;
}
