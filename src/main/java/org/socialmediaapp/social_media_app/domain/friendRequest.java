package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class friendRequest {
    private userDTO sourceUser;
    private Date sendDate;
    private Integer requestID;
    private Integer receiverID;

    public friendRequest(userDTO sourceUser, Date sendDate,Integer receiverID) {
        this.sourceUser = sourceUser;
        this.sendDate = sendDate;
        this.receiverID = receiverID;
    }

    public friendRequest(userDTO sourceUser, Date sendDate, Integer requestID,Integer receiverID) {
        this.sourceUser = sourceUser;
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

    public userDTO getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(userDTO sourceUser) {
        this.sourceUser = sourceUser;
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
