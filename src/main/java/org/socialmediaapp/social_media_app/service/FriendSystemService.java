package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.FriendSystemDao;
import org.socialmediaapp.social_media_app.domain.friendRequest;

import java.util.Date;
import java.util.List;

public class FriendSystemService {

    private FriendSystemDao friendSystemDao ;

    public FriendSystemService(FriendSystemDao friendSystemDao) {
        this.friendSystemDao = friendSystemDao;
    }


    public boolean createFriendRequest(Integer SourceUserID , String SourceUserName,  Integer receiverID){

        friendRequest fReq = new friendRequest(SourceUserID,SourceUserName,new Date() , receiverID);

        return this.friendSystemDao.createFriendRequest(fReq);

    }

    public boolean acceptFriendRequest(friendRequest friendReq){
        // friend request will be retrieved in UI
        return this.friendSystemDao.acceptFriendRequest(friendReq);
    }


    public boolean ignoreFriendRequest(friendRequest friendReq){
        return this.friendSystemDao.ignoreFriendRequest(friendReq);
    }

    public List<friendRequest> getUserFriendRequests(Integer userID){
        return this.friendSystemDao.getUserFriendRequests(userID);
    }


}
