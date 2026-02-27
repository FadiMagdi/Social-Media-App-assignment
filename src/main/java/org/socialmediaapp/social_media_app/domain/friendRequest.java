package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class friendRequest {
    private Integer SourceUserID;
    private String SourceUserName;
    private Date sendDate;
    private Integer requestID;
    private Integer receiverID;

    public friendRequest(Integer SourceUserID , String SourceUserName, Date sendDate,Integer receiverID) {
        this.SourceUserID = SourceUserID;
        this.SourceUserName = SourceUserName;
        this.sendDate = sendDate;
        this.receiverID = receiverID;
    }

    public friendRequest(Integer SourceUserID , String SourceUserName, Date sendDate, Integer requestID,Integer receiverID) {
        this.SourceUserID = SourceUserID;
        this.SourceUserName = SourceUserName;
        this.sendDate = sendDate;
        this.requestID = requestID;
        this.receiverID = receiverID;
    }

    public Integer getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Integer receiverID) {
        this.receiverID = receiverID;
    }

    public Integer getSourceUserID() {
        return SourceUserID;
    }

    public void setSourceUserID(Integer sourceUserID) {
        SourceUserID = sourceUserID;
    }

    public String getSourceUserName() {
        return SourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        SourceUserName = sourceUserName;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }
}
