package com.project.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CommunityComment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
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
    @JsonIgnore
    private CommunityComment parent;

    @Column(name = "child_count")
    private Long childCount;

    @Column(name = "is_deleted")
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status isDeleted;

    @Column(name = "is_privated")
    @Enumerated(value = EnumType.STRING)
    private EnumStatus.Status isPrivated;

}
