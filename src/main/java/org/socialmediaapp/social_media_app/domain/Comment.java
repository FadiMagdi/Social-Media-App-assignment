package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class Comment {
    public User userMadeComment;
    public String commentText;
    public Date commentDate;
    private Integer commentID;

    public Comment(User userMadeComment, String commentText, Date commentDate) {
        this.userMadeComment = userMadeComment;
        this.commentText = commentText;
        this.commentDate = commentDate;
    }

    public Comment(User userMadeComment, String commentText, Date commentDate, Integer commentID) {
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
}
