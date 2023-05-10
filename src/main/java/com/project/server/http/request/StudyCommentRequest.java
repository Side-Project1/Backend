package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.EnumStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyCommentRequest {

        @Schema(description = "댓글" , example = "댓글남겨요~")
        private String comments;
        @Schema(description = "스터디 게시글 일련번호" , example = "1")
        private Long studyId;
        @Schema(description = "부모 댓글 일련번호" , example = "1")
        private Long commentId;
        @Schema(description = "비밀 댓글인가" , example = "1")
        private EnumStatus.Status isPrivated;
    }

