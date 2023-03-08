package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
@Getter
@Setter
@Data
public class CommentRequest {
    @Schema(description = "댓글" , example = "댓글남겨요~")
    private String content;
    private Long studyId;

    private String userName;

    private Long parentId;
}
