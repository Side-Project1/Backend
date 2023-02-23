package com.project.server.entity;

import com.project.server.security.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_sn", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column
    private String userId;
    @Column
    private String password;
    @Column(nullable = false)
    private String userName;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private String birthday;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id")
    private UserToken userToken;
}
