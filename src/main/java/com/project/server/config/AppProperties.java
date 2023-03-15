package com.project.server.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Getter
@Component
public class AppProperties {
    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;
    @Value("${app.auth.tokenExpirationMsec}")
    private long tokenExpirationMsec;
    @Value("#{'${app.oauth2.authorizedRedirectUris}'.split(',')}")
    private List<String> authorizedRedirectUris;

}