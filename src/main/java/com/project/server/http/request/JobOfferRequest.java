package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobOfferRequest {
    private String title;
    private String place;
    private LocalDateTime stDt;
    private LocalDateTime enDt;
    private LocalDateTime wStT;
    private LocalDateTime wEnT;
    private LocalDateTime deadLine;
    private boolean deadLineCheck;
    private String recruitment;
    private String workContent;
    private String qualificationsNeeded;
    private Integer personNum;
    private String preferential;
    private String phone;
    private String email;
    private String content;
    private String salary;

}
