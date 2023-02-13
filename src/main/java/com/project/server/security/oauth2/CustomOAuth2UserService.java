package com.project.server.security.oauth2;


import com.project.server.entity.Role;
import com.project.server.entity.Users;
import com.project.server.exception.OAuth2AuthenticationProcessingException;
import com.project.server.repository.UserRepository;
import com.project.server.security.AuthProvider;
import com.project.server.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuthAttributes attributes = OAuthAttributes.of(oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(), oAuth2User.getAttributes());
        if(attributes.getEmail() == null) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        Optional<Users> userOptional = userRepository.findByEmail(attributes.getEmail());
        Users users;
        if(userOptional.isPresent()) {
            users = userOptional.get();
            if(!users.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        users.getProvider() + " account. Please use your " + users.getProvider() +
                        " account to login.");
            }
            users = updateExistingUser(users, attributes);
        } else {
            users = registerNewUser(oAuth2UserRequest, attributes);
        }

        return CustomUserPrincipal.create(users, oAuth2User.getAttributes());
    }

    private Users registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuthAttributes attributes) {
        Users users = Users.builder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(attributes.getNameAttributeKey())
                .name(attributes.getName())
                .email(attributes.getEmail())
                .imageUrl(attributes.getProfile())
                .role(Role.USER)
                .build();

        return userRepository.save(users);
    }

    private Users updateExistingUser(Users users, OAuthAttributes attributes) {
        users.setName(attributes.getName());
        users.setImageUrl(attributes.getProfile());
        users.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(users);
    }
}