package com.project.server.http.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    // 수신자
    private String receiver;
}
