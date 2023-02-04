package com.project.server.entity;

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
public class Token {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    public User getUser() {
        return user;    }
    @Builder
    public Token(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getRefreshToken(String refreshToken) { return refreshToken;    }
}