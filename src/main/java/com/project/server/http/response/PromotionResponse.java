package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Comment;
import com.project.server.entity.JobCategory;
import com.project.server.entity.Promotions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PromotionResponse {
    private String title;
    private String contents;
    private String writer;
    private List<Comment> commentList;
    private List<JobCategory> jobCategoryList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String contents;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updateDate;

        public Response(Promotions promotions) {
            this.id = promotions.getId();
            this.title = promotions.getTitle();
            this.contents = promotions.getContents();
            this.createdDate = promotions.getCreatedDate();
            this.updateDate = promotions.getUpdatedDate();
        }
    }


    @Builder
    public PromotionResponse(String title, String contents, String writer, List<Comment> commentList, List<JobCategory> jobCategoryList, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.commentList = commentList;
        this.jobCategoryList = jobCategoryList;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }
}
