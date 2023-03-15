package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
@Getter
@Setter
public class ResumeRequest {
    @Schema(description = "전공" , example = "성악")

    private String major;

    private String career;

    private String school;

    private String award;

    private String profileImgUrl;

    private String area;

    private String contents;

    private String portfolioUrl;

    private List<MultipartFile> file;

    public Resume ToEntity(){
        return Resume.builder()
                .major(this.major)
                .career(this.career)
                .school(this.school)
                .award(this.award)
                .profileImgUrl(this.profileImgUrl)
                .area(this.area)
                .contents(this.contents)
                .portfolioUrl(this.portfolioUrl)
                .build();
    }
}
