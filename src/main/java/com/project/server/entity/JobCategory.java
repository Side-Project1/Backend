package com.project.server.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobCategory {
    @Id
    @GeneratedValue
    @Column(name = "job_category_id")
    private Long id;
    @Column(name = "parent_category")
    private String parentCategory;
    @Column(name = "sub_category")
    private String subCategory;
    public JobCategory(Long id) {
        this.id = id;
    }
}
