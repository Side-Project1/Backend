package com.project.server.entity;

import java.io.Serializable;
import java.util.UUID;

public class ScrapId implements Serializable {
    private UUID users;
    private Long study;
    public ScrapId(){}
    public ScrapId(UUID users, Long study){
        super();
        this.users=users;
        this.study=study;
    }
}
