package com.project.server.entity;

import com.project.server.http.request.CommentRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="study_id")
//    private Study study;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    //comment는 자기 자신을 selfjoin 하고 있음, 부모 댓글 삭제될 시 하위 댓글들도 같이 삭제
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
    @Column
    private String writer;
    @Column
    private String content;


    public void update(CommentRequest commentRequest) {
        this.content = commentRequest.getContent();
    }


//    public static Comment createComment(String content, Study study, User user, Comment parent) {
//        Comment comment = new Comment();
//        comment.content = content;
//        comment.study = study;
//        comment.user = user;
//        comment.parent = parent;
//        comment.isDeleted = DeleteStatus.N;
//        return comment;
//    }

    public void changeDeletedStatus(DeleteStatus deleteStatus) {
        this.isDeleted = deleteStatus;
    }
}
