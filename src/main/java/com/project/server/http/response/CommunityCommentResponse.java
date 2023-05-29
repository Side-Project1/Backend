package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.CommunityComment;
import com.project.server.entity.EnumStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityCommentResponse {
    private Long commentId;
    private Long communityId;
    private String userId;
    private String comments;
    private Long ref;
    private Long refStep;
    private Long parentId;
    private Long childCount;
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status isDeleted;
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status isPrivated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Getter
    public static class UserResponse {
        private Long commentId;
        private Long communityId;

        private String comments;
        private String isPrivated;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        public UserResponse(CommunityComment communityComment) {
            this.commentId = communityComment.getId();
            this.communityId = communityComment.getCommunity().getId();
            this.comments = communityComment.getComments();
            this.isPrivated = communityComment.getIsPrivated().getValue();
            this.createdDate = communityComment.getCreatedDate();
        }

    }
}
