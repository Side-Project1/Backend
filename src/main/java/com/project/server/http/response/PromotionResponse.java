package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Comment;
import com.project.server.entity.JobCategory;
import com.project.server.entity.Promotions;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PromotionResponse {
    private String title;
    private String contents;
    private String writer;
    private List<JobCategory> jobCategoryList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    @Getter
    public static class UserResponse {
        private Long id;
        private String title;
        private String contents;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updateDate;

        public UserResponse(Promotions promotions) {
            this.id = promotions.getId();
            this.title = promotions.getTitle();
            this.contents = promotions.getContents();
            this.createdDate = promotions.getCreatedDate();
            this.updateDate = promotions.getUpdatedDate();
        }
    }


    public PromotionResponse(Promotions promotions) {
        this.title = promotions.getTitle();
        this.contents = promotions.getContents();
        this.writer = promotions.getUser().getUserId();
        this.jobCategoryList = promotions.getJobCategoryList();
        this.createdDate = promotions.getCreatedDate();
        this.updateDate = promotions.getUpdatedDate();
    }
}
