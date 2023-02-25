package com.project.server.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseBoard {
    @Column
    private Integer viewCount;
    @Column
    private String wrtrId;
    @CreatedDate
    private LocalDateTime createdDate;
    @Column
    private String mdfrId;
    @LastModifiedDate
    private LocalDateTime updatedDate;
}