package com.project.server.security;

import com.project.server.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomUserPrincipal implements OAuth2User, UserDetails {
    private UUID id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private Users users;

    public Users getUser() {
        return users;
    }

    public CustomUserPrincipal(UUID id, String email, String password, Collection<? extends GrantedAuthority> authorities, Users users) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.users = users;
    }

    public static CustomUserPrincipal create(Users users) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserPrincipal(
                users.getId(),
                users.getEmail(),
                users.getPassword(),
                authorities,
                users
        );
    }

    public static CustomUserPrincipal create(Users users, Map<String, Object> attributes) {
        CustomUserPrincipal userPrincipal = CustomUserPrincipal.create(users);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        System.out.println("이건데?");
        return String.valueOf(id);
    }
}
