package com.project.server.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String email;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String sns;
}
