package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyRequest {
    private String title; //제목
    private String author;

    private int max; //인원 수

    private String region; //지역

    private String contents; // 자유 양식

    private Integer view;
}
