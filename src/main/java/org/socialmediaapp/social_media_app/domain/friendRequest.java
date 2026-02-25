package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

public class friendRequest {
    private User sourceUser;
    private Date sendDate;
    private Integer requestID;

    public friendRequest(User sourceUser, Date sendDate) {
        this.sourceUser = sourceUser;
        this.sendDate = sendDate;
    }

    public friendRequest(User sourceUser, Date sendDate, Integer requestID) {
        this.sourceUser = sourceUser;
        this.sendDate = sendDate;
        this.requestID = requestID;
    }



}
