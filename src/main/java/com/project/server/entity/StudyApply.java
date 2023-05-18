package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class StudyApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String introduction;

    @Enumerated(EnumType.STRING)
    private EnumStatus.StudyApplyStatus applyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    @JsonIgnore
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_sn")
    @JsonIgnore
    private Users users;

}
