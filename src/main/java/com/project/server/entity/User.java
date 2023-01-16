package com.project.server.entity;

import com.project.server.security.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true, nullable = false)
    private String name;
    @Column
    private String password;
    @Column
    private String imageUrl;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AuthProvider provider;
    @Column
    private String providerId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
