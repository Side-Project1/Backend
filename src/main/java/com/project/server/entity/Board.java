package com.project.server.entity;

import javax.persistence.*;

@Entity
public class Board extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String title;
    @Column
    private String contents;
}
