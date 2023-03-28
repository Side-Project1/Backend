package com.project.server.entity;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Type {
    STUDY("TYPE_STUDY"),PROMOTION("TYPE_PROMOTION");
    private final String type;
}

