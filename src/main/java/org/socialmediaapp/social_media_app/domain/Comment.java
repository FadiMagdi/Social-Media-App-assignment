package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class Comment {
    public userDTO userMadeComment;
    public String commentText;
    public Date commentDate;
    private Integer commentID;

    public Comment(userDTO userMadeComment, String commentText, Date commentDate) {
        this.userMadeComment = userMadeComment;
        this.commentText = commentText;
        this.commentDate = commentDate;
    }

    public Comment(userDTO userMadeComment, String commentText, Date commentDate, Integer commentID) {
        this.userMadeComment = userMadeComment;
        this.commentText = commentText;
        this.commentDate = commentDate;
        this.commentID = commentID;
    }

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
    }

    public userDTO getUserMadeComment() {
        return userMadeComment;
    }

    public void setUserMadeComment(userDTO userMadeComment) {
        this.userMadeComment = userMadeComment;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}
