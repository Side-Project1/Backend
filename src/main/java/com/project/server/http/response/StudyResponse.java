package com.project.server.http.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.EnumStatus;
import com.project.server.entity.JobCategory;
import com.project.server.entity.Promotions;
import com.project.server.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyResponse {
    private String title;
    private String contents;
    private String author;
    private int max;
    private String region;

    private String recruitment;
    private Long viewCount;
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
        private String author;
        private int max;
        private String region;
        private Long viewCount;
        private String recruitment;
        private List<JobCategory> jobCategoryList;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updateDate;



        public UserResponse(Study study) {
            this.title = study.getTitle();
            this.author=study.getAuthor();
            this.max=study.getMax();
            this.region = study.getRegion();
            this.contents = study.getContents();
            this.recruitment=study.getRecruitment().getValue();
            this.viewCount=study.getViewCount();
            this.jobCategoryList = study.getJobCategoryList();
            this.createdDate = study.getCreatedDate();
            this.updateDate = study.getUpdatedDate();
        }
    }

        public StudyResponse(Study study) {
            this.title = study.getTitle();
            this.author=study.getAuthor();
            this.max=study.getMax();
            this.region = study.getRegion();
            this.contents = study.getContents();
            this.recruitment=study.getRecruitment().getValue();
            this.viewCount=study.getViewCount();
            this.jobCategoryList = study.getJobCategoryList();
            this.createdDate = study.getCreatedDate();
            this.updateDate = study.getUpdatedDate();

    }
}
