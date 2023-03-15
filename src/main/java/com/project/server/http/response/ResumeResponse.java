package com.project.server.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResumeResponse {
    @Schema(description = "전공" , example = "성악")

    private String major;

    private String career;

    private String school;

    private String award;

    private String profileImg;

    private String area;

    private String contents;

    private String portfolio;

    private MultipartFile file;

}
