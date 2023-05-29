package com.project.server.http.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Community;
import com.project.server.entity.JobCategory;
import com.project.server.entity.Photo;
import com.project.server.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityResponse {
    private String title;
    private String contents;
    private String author;
    private Long viewCount;

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

        private Long viewCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updateDate;



        public UserResponse(Community community) {
            this.title = community.getTitle();
            this.author=community.getAuthor();
            this.contents = community.getContents();
            this.viewCount=community.getViewCount();
            this.createdDate = community.getCreatedDate();
        }
    }

        public CommunityResponse(Community community) {
            this.title = community.getTitle();
            this.author=community.getAuthor();
            this.contents = community.getContents();
            this.viewCount=community.getViewCount();
            this.createdDate = community.getCreatedDate();


    }
}
