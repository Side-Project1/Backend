package com.project.server.config;





import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app")
@Component
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Setter
    @Getter
    public static class Auth {
        @Value("${app.auth.tokenSecret}")
        private String tokenSecret;
        @Value("${app.auth.tokenExpirationMsec}")
        private long tokenExpirationMsec;
    }

    @Setter
    @Getter
    public static final class OAuth2 {
        @Value("#{'${app.oauth2.authorizedRedirectUris}'.split(',')}")
        private List<String> authorizedRedirectUris;
        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }
}