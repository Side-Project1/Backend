package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Link;
import com.project.server.entity.Photo;
import com.project.server.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
public class ResumeRequest {
    private String title;

    private String profileUrl;
    private String certificate;

    private String career;

    private String school;

    private String job;

    private List<String> links;

}
