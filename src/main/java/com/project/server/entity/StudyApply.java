package com.project.server.entity;

import com.project.server.repository.Study.StudyApplyRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StudyApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column
//    private String name;

    @Column
    private String introduction;

    @Enumerated(EnumType.STRING)
    private StudyApplyStatus applyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_sn")
//    private User member;


}
