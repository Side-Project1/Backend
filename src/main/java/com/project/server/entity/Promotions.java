package com.project.server.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotions extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "promotion_id")
    private Long id;
    @Column
    private String title;
    @Column
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sn")
    private User user;
    @OneToMany(mappedBy = "promotions", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "PROMOTION_CATEGORY_TABLE",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "job_category_id")
    )
    @Builder.Default    // 빌더시 인스턴스 기본 값 세팅
    private List<JobCategory> jobCategoryList = new ArrayList<>();


}
