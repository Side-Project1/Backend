package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.server.http.request.ResumeRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "user")

public class Resume extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //UUID로 해야하나? -> 하는 걸로

    @Column
    private String major;

    @Column
    private String career;

    @Column
    private String school;

    @Column
    private String award;

    @Column
    private String profileImgUrl;

    @Column
    private String area;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    //@ElementCollection
    @Column
    private String portfolioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    public void createdByUser(User user) {
        this.user = user; //like setter
    }


    // 업데이트 메소드
    public void update(ResumeRequest request) {
        this.major = request.getMajor();
        this.career = request.getCareer();
        this.school=request.getSchool();
        this.award=request.getAward();
        this.profileImgUrl=request.getProfileImgUrl();
        this.area=request.getArea();
        this.contents=request.getContents();
        this.portfolioUrl=request.getPortfolioUrl();
    }

}
