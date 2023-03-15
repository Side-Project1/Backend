package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Study extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title; //제목
    @Column
    private String author; //작성자
    @Column
    private String max; //인원 수

    @Column
    private String region; //지역

    @Column
    private String contents; // 자유 양식

    @Column(name="VIEW_COUNT")
    private int viewCount; //조회수

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name="user_id")
    private User user;

//    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StudyCategory category; //카테고리



    public void createdByUser(User user) {
        this.user = user; //like setter
    }


//    public void update(StudyRequest studyRequest) {
//        this.title=studyRequest.getTitle();
//        this.max=studyRequest.getMax();
//        this.region=studyRequest.getRegion();
//        this.contents=studyRequest.getContents();
//        this.category=studyRequest.getCategory();
//    }


    public void update(String title, String max, String region, String contents, StudyCategory category) {
        this.title=title;
        this.max=max;
        this.region=region;
        this.contents=contents;
        this.category=category;
    }
}
