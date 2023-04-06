package com.project.server.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Promotions;
import com.project.server.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private UUID id;
    private String userId;
    private String userName;
    private String email;
    private String birthday;
    private String gender;
    private String job;
    private List<PromotionResponse.Response> promotionResponses;

    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
        this.job = user.getJob();
        this.promotionResponses = user.getPromotionsList().stream()
                .map(promotions -> new PromotionResponse.Response(promotions))
                .collect(Collectors.toList());
    }
}
