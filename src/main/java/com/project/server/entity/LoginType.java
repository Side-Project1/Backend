package com.project.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
    GIT("GIT"), KAKAO("KAKAO"), LOCAL("LOCAL");
    private final String type;
}
