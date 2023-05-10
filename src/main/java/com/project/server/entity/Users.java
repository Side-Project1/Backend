package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.server.security.AuthProvider;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users extends BaseTime {
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

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();


    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StudyComment> studycommentList = new ArrayList<>();

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Promotions> promotionsList = new ArrayList<>();


    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    @Builder.Default
    private List<Study> studies =new ArrayList<>();


    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    @JsonBackReference //순환참조 방지
    private List<StudyMember> members =new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    @JsonBackReference //순환참조 방지
    private List<Scrap> scrapList =new ArrayList<>();



//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "user_scrap",
//            joinColumns = @JoinColumn(name = "user_sn"),
//            inverseJoinColumns = @JoinColumn(name = "study_id"))
//    private Set<Study> scraps = new HashSet<>();
//
//    public void addScrap(Study study) {
//        scraps.add(study);
//        study.getScrappers().add(this);
//    }
//
//    public void removeScrap(Study study) {
//        scraps.remove(study);
//        study.getScrappers().remove(this);
//    }


}
