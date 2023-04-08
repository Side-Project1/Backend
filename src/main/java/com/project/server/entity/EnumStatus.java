package com.project.server.entity;

import lombok.Getter;

public class EnumStatus {
    @Getter
    public enum Status {
        Y("Y"),N("N");
        private final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
