package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyPageResponse {
    @Schema(description = "제목" , example = "제목")
    private String title;

    @Schema(description = "내용" , example = "내용")
    private String contents;

    @Schema(description = "작성자" , example = "나")
    private String userId;

    @Schema(description = "지역" , example = "서울")
    private String region;

    private List<Long> subCategory;
    @Schema(description = "작성 시간" , example = "2023-07-16")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;


}
