package com.project.server.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.*;

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

    @Column(name = "recruitment")
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status recruitment;

    @Column(name="VIEW_COUNT")
    private Long viewCount; //조회수



    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL ,fetch = FetchType.LAZY,orphanRemoval = true )
    private List<Photo> studyPhotoList =new ArrayList<>();

    @OneToMany(mappedBy = "study",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<StudyApply> applyList = new ArrayList<>();

    @OneToMany(mappedBy = "study",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<StudyMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "study",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StudyComment> commentList = new ArrayList<>();

    @ManyToOne
    @JsonManagedReference
    @JsonIgnore
    @JsonBackReference
    @JoinColumn(name="user_sn")
    private Users users;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "STUDY_CATEGORY_TABLE",
            joinColumns = @JoinColumn(name = "study_id"),
            inverseJoinColumns = @JoinColumn(name = "job_category_id")
    )
    private List<JobCategory> jobCategoryList = new ArrayList<>();


    @Builder
    public Study(String title, String author, int max, String region, String contents,EnumStatus.Status recruitment ,Users users, Long viewCount) {
        this.title = title;
        this.author=author;
        this.max=max;
        this.region=region;
        this.contents=contents;
        this.recruitment=recruitment;
        this.users = users;
        this.viewCount=viewCount;

    }

    public void writePhoto(Photo photo){
        studyPhotoList.add(photo);
        photo.setStudy(this);

    }


}
