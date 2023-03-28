package com.project.server.http.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.server.entity.Comment;
import com.project.server.entity.StudyCategory;
import com.project.server.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import jdk.jfr.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class StudyResponse {

    private Long id;
    private String title; //제목

    private String author; //작성자

    private String max; //인원 수

    private String region; //지역


    private int viewCount; //조회수
    private StudyCategory category; //카테고리




//    @QueryProjection
//    public StudyOneResponse(Long id, String title, String author, String max, String region, String contents, int viewCount,StudyCategory category) {
//        this.id = id;
//        this.title = title;
//        this.author = author;
//        this.max = max;
//        this.region = region;
//        this.contents = contents;
//        this.viewCount = viewCount;
//        this.category = category;
//    }
}
