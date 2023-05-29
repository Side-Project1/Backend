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
public class CommunityRequest {
    @Schema(description = "제목" , example = "커뮤니티 글 제목")
    private String title; //제목
    @Schema(description = "내용" , example = "커뮤니키 글 내용")
    private String contents; // 자유 양식

}
