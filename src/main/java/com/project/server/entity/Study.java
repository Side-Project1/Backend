package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.server.http.request.StudyRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Study extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title; //제목
    @Column
    private String author; //작성자
    @Column
    private int max; //인원 수

    @Column
    private String region; //지역

    @Column
    private String contents; // 자유 양식

    @Column(name="VIEW_COUNT")
    private Long viewCount; //조회수

    @Column
    private String owner;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL ,orphanRemoval = true )
    @JsonBackReference
    private List<Photo> studyPhotoList =new ArrayList<>();

    @OneToMany(mappedBy = "study",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<StudyApply> applyList = new ArrayList<>();

    @OneToMany(mappedBy = "study",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<StudyMember> members = new ArrayList<>();

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name="user_sn")
    private User user;
    public void createdByUser(User user) {
        this.user = user; //like setter
    }

    @Builder
    public Study(String title, String author,int max, String region, String contents,User user,Long viewCount, String owner,List<Photo> studyPhotoList) {
        this.title = title;
        this.author=author;
        this.max=max;
        this.region=region;
        this.contents=contents;
        this.user=user;
        this.owner=owner;
        this.viewCount=viewCount;
        this.studyPhotoList = studyPhotoList != null ? studyPhotoList : new ArrayList<>();

    }


    public void writePhoto(Photo photo){
        studyPhotoList.add(photo);
        photo.setStudy(this);

    }

}
