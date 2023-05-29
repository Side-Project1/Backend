package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.JobCategory;
import com.project.server.entity.StudyComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityPageResponse {
    @Schema(description = "일련번호", example = "1")
    private Long id;
    @Schema(description = "제목", example = "제목")
    private String title;

    @Schema(description = "작성 시간", example = "2023-07-16")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Schema(description = "조회 수", example = "조회 수")
    private Long viewCount;
    @Schema(description = "댓글 수", example = "댓글 수")
    private List<StudyComment> commentList = new ArrayList<>();


}