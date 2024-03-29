package com.project.server.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    @Schema(description = "수신자" , example = "dnd@naver.com")
    private String receiver;
}
