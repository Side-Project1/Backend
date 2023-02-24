package com.project.server.entity;

import com.project.server.http.request.ResumeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //UUID로 해야하나?

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

    @Column
    private String portfolioUrl;



    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", s3Url='" + profileImgUrl + '\'' +
                '}';


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
