package com.project.server.http.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindPwRequest {
    @Schema(description = "사용자 아이디" , example = "dnd2dnd2")
    private String userId;
    @Schema(description = "사용자 이메일" , example = "dnd2dnd2")
    private String email;
}
