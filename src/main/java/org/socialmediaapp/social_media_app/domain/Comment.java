package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class Comment {
    private Integer userID ;
    public String userName;
    public String commentText;
    public Date commentDate;
    private Integer commentID;

    public Comment(Integer userID, String userName, String commentText, Date commentDate, Integer commentID) {
        this.userID = userID;
        this.userName = userName;
        this.commentText = commentText;
        this.commentDate = commentDate;
        this.commentID = commentID;
    }


    public Comment(String userName, String commentText, Date commentDate, Integer commentID) {
        this.userName = userName;
        this.commentText = commentText;
        this.commentDate = commentDate;
        this.commentID = commentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
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
