package com.project.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="promotion_id")
    private Promotions promotions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_sn")
    private Users users;

    @Column
    private String comments;

    @Column
    private Long ref;

    @Column(name = "ref_step")
    private Long refStep;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", referencedColumnName = "comment_id")
    private Comment parent;

    @Column(name = "child_count")
    private Long childCount;

    @Column(name = "is_deleted")
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status isDeleted;

    @Column(name = "is_privated")
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status isPrivated;


}
