package com.project.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoType {
    STUDY("STUDY"), RESUME("RESUME");
    private final String photoType;
}

