package com.project.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeleteStatus {
    Y("Y"),N("N");
    private final String value;
}
