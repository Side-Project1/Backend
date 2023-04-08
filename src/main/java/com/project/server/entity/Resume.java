package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.server.http.request.ResumeRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor

@Data
@ToString(exclude = "user")

public class Resume extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title; //이력서 제목

    @Column
    private String certificate; //자격증
    @Column
    private String career; //경력

    @Column
    private String school; //학력
    @Column(columnDefinition = "TEXT", nullable = false)
    private String job; //희망 직무

    @Column
    private String link; //링크

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photoList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_sn")
    @JsonBackReference
    private User user;

    public void createdByUser(User user) {
        this.user = user; //like setter
    }
    @Builder
    public Resume(String title, String certificate, String career, String school, String job,String link,User user, List<Photo> photoList) {
        this.title = title;
        this.certificate=certificate;
        this.career=career;
        this.school=school;
        this.job=job;
        this.link=link;
        this.user=user;
        this.photoList = photoList != null ? photoList : new ArrayList<>();

    }
    // 업데이트 메소드
    public void update(ResumeRequest request) {
        this.title = request.getTitle();
        this.certificate = request.getCertificate();
        this.career = request.getCareer();
        this.school=request.getSchool();
        this.job=request.getJob();
        this.link=request.getLink();
    }


    public void writePhoto(Photo photo){
        photoList.add(photo);
        photo.setResume(this);

    }


}
