package com.project.server.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.server.entity.Users;
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

    private List<StudyResponse.UserResponse> studyResponse;
    private List<CommunityResponse.UserResponse> communityResponse;
    private List<StudyCommentResponse.UserResponse> studycommentResponse;

    public UserResponse(Users users) {
        this.id = users.getId();
        this.userId = users.getUserId();
        this.userName = users.getUserName();
        this.email = users.getEmail();
        this.birthday = users.getBirthday();
        this.gender = users.getGender();
        this.job = users.getJob();
        this.commentResponses = users.getCommentList().stream()
                .filter(comment -> comment.getIsDeleted().getValue().equals("N"))
                .map(comment -> new CommentResponse.UserResponse(comment))
                .collect(Collectors.toList());
        this.studycommentResponse = users.getStudycommentList().stream()
                .filter(studyComment -> studyComment.getIsDeleted().getValue().equals("N"))
                .map(studyComment -> new StudyCommentResponse.UserResponse(studyComment))
                .collect(Collectors.toList());
        this.promotionResponses = users.getPromotionsList().stream()
                .map(promotions -> new PromotionResponse.UserResponse(promotions))
                .collect(Collectors.toList());

        this.studyResponse = users.getStudies().stream()
                .map(study -> new StudyResponse.UserResponse(study))
                .collect(Collectors.toList());

        this.communityResponse = users.getCommunityList().stream()
                .map(community -> new CommunityResponse.UserResponse(community))
                .collect(Collectors.toList());

    }


}
