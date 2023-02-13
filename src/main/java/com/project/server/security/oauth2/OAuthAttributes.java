package com.project.server.security.oauth2;

import com.project.server.exception.OAuth2AuthenticationProcessingException;
import com.project.server.security.AuthProvider;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String profile;
//    public OAuth2UserInfo(Map<String, Object> attributes) {
//        this.attributes = attributes;
//    }
//
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String profile) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public static OAuthAttributes of(String registrationId, String nameAttributeKey, Map<String, Object> attributes) {
        System.out.println(attributes);
        System.out.println(registrationId);
        System.out.println(nameAttributeKey);
        if(registrationId.equalsIgnoreCase(AuthProvider.kakao.toString())) {
            return ofKakao(nameAttributeKey, attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.naver.toString())) {
            return ofNaver(nameAttributeKey, attributes);
        }
        else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

    private static OAuthAttributes ofKakao(String nameAttributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .nameAttributeKey(nameAttributeKey)
                .attributes(attributes)
                .build();
    }

//    private static OAuthAttributes ofGithub(String nameAttributeKey, Map<String, Object> attributes) {
//        String gitAccount = (String) attributes.get("login");
//        System.out.println(gitAccount);
////        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
//
//        return OAuthAttributes.builder()
//                .name(gitAccount)
//                .email(gitAccount)
//                .nameAttributeKey(nameAttributeKey)
//                .attributes(attributes)
//                .build();
//    }

    private static OAuthAttributes ofNaver(String nameAttributeKey, Map<String, Object> attributes) {
        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
//        System.out.println(gitAccount);
//        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name(naverAccount.get("name").toString())
                .email(naverAccount.get("email").toString())
                .nameAttributeKey(naverAccount.get("name").toString())
                .attributes(attributes)
                .build();
    }
}