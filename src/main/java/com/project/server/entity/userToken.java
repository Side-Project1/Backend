package com.project.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class userToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_token_id", columnDefinition = "BINARY(16)")
    private UUID id;
    private String refreshToken;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
}
