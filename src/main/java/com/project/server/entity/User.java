package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.server.security.AuthProvider;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name = "user_sn", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column
    private String userId;
    @Column
    private String password;
    @Column
    private String userName;
    @Column
    private String phone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String birthday;    // 생일
    @Column
    private String gender;      // 성별
    @Column
    private String job;         // 직업
    @Column
    private String path;        // 가입 경로
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AuthProvider provider;
    @Column
    private String providerId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Promotions> promotionsList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Resume> resumes=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference //순환참조 방지
    private List<Study> studies =new ArrayList<>();

//    public void addComment(Comment comment) {
//        if (comment != null) {
//            this.comments.add(comment);
//            comment.setUser(this);
//        }
//    }


    public void writeStudy(Study study){
        this.studies.add(study);
        study.createdByUser(this);
    }

    public void writeResume(Resume resume){
        this.resumes.add(resume);
        resume.createdByUser(this);
    }

}
