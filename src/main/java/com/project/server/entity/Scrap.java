package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(ScrapId.class)
public class Scrap{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_sn")
    private Users users;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "study_id")
    private Study study;
}
