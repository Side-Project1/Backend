package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.StudyCategory;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyRequest {
    private String title; //제목
    private String author;

    private int max; //인원 수

    private String region; //지역

    private String contents; // 자유 양식

//    private StudyCategory category; //카테고리

    private Integer view;
}
