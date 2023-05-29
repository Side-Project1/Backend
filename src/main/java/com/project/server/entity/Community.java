package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Community extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String contents;

    @Column(name="VIEW_COUNT")
    private Long viewCount; //조회수

    @ManyToOne
    @JsonManagedReference
    @JsonIgnore
    @JsonBackReference
    @JoinColumn(name="user_sn")
    private Users users;

    @OneToMany(mappedBy = "study",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StudyComment> commentList = new ArrayList<>();
}
