package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.PostManagementDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.User;
import org.socialmediaapp.social_media_app.domain.friendRequest;
import org.socialmediaapp.social_media_app.domain.userDTO;

import java.util.List;

public class UserService {

    private UserDao userDao;
    private PostManagementService postManagementService;
    private FriendSystemService friendSystemService;

    public UserService(UserDao userDao, PostManagementService postManagementService, FriendSystemService friendSystemService) {
        this.userDao = userDao;
        this.postManagementService = postManagementService;
        this.friendSystemService = friendSystemService;
    }

    public userDTO getUserDTOByID(Integer userID){
        userDTO retrieved =  this.userDao.getUserDTOByID(userID);
        List<Post> userPosts = this.postManagementService.getPostsByUserIDAndPrivacy(userID,"public");
        retrieved.setUserPosts(userPosts);
        return retrieved;
    }


    public boolean createUser(String email, String password, String userName, int age,String bio,String image_path){
        return this.userDao.createUser( email, password, userName,  age, bio,image_path);
    }

    public boolean createProfile(Integer userID , String bio , String image_Path){
        return this.userDao.createProfile(userID,bio,image_Path);
    }

    public User getUserByEmail(String Email){
        User loggingIn =  this.userDao.getUserByEmail(Email);

        List<friendRequest> frs = this.friendSystemService.getUserFriendRequests(loggingIn.getUserID());
        List<Post> userPosts = this.postManagementService.getPostsByUserID(loggingIn.getUserID());
        List<Post> NewsFeed = this.postManagementService.getPostsForNewsFeed(loggingIn.getUserID());

        loggingIn.setPostsReceived(NewsFeed);
        loggingIn.setPostsMade(userPosts);
        loggingIn.setFriendRequests(frs);


        return loggingIn;

    }

    public List<userDTO> getUserFriends(Integer userID){
        List<userDTO> userFriends =this.userDao.getUserFriends(userID);
        for(int i=0 ; i<userFriends.size();i++){
            userDTO currentFriend = userFriends.get(i);
            List<Post> userPostsForFriends = this.postManagementService.getPostsByUserIDAndPrivacy(currentFriend.getUserID(),"friends only");
            List<Post> publicPosts = this.postManagementService.getPostsByUserIDAndPrivacy(currentFriend.getUserID(), "public");

            userPostsForFriends.addAll(publicPosts);

            currentFriend.setUserPosts(userPostsForFriends);
        }

        return userFriends;
    }



}
