package com.project.server.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.User;
import lombok.Getter;
import lombok.Setter;

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
    private List<CommentResponse.UserResponse> commentResponses;
    private List<PromotionResponse.UserResponse> promotionResponses;

    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
        this.job = user.getJob();
        this.commentResponses = user.getCommentList().stream()
                .filter(comment -> comment.getIsDeleted().getValue().equals("N"))
                .map(comment -> new CommentResponse.UserResponse(comment))
                .collect(Collectors.toList());
        this.promotionResponses = user.getPromotionsList().stream()
                .map(promotions -> new PromotionResponse.UserResponse(promotions))
                .collect(Collectors.toList());
    }
}
