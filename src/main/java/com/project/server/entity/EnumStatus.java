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

    @Getter
    public enum RecuritmentStatus {
        모집중("모집"),모집마감("모집마감");
        private final String value;

        RecuritmentStatus(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum StudyApplyStatus {
        APPROVED("APPROVED"),REJECTED("REJECTED"),PENDING("PENDING");

        private final String studyApplicationStatus;
        StudyApplyStatus(String studyApplicationStatus){this.studyApplicationStatus=studyApplicationStatus;}
    }

}
