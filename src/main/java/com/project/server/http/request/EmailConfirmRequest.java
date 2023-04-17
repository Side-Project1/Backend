package com.project.server.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class EmailConfirmRequest {
    @Schema(description = "수신자" , example = "dnd@naver.com")
    private String receiver;
    @Schema(description = "확인번호" , example = "123456")
    private Integer number;
}
