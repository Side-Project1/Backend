package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {
    private Long commentId;
    private Long promotionId;
    private String userId;
    private String comments;
    private Long ref;
    private Long refStep;
    private Long parentId;
    private Long childCount;
    @Enumerated(value = EnumType.STRING)
    private Status isDeleted;
    @Enumerated(value = EnumType.STRING)
    private Status isPrivated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
}
