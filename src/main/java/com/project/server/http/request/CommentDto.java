package com.project.server.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

    private Long id;
    private String content;
    private String userName;
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Long id, String content, String userName) {
        this.id = id;
        this.content = content;
        this.userName = userName;

    }

//    public static CommentDto convertCommentToDto(Comment comment) {
//        return comment.getIsDeleted() == DeleteStatus.Y ?
//                new CommentDto(comment.getId(), "삭제된 댓글입니다.", null, null) :
//                new CommentDto(comment.getId(), comment.getContent(), comment.getUser().getUserName());
//    }
}
