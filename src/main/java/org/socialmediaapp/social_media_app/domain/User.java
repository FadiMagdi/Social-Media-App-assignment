package org.socialmediaapp.social_media_app.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Integer userID;
    private String email;
    private String password;
    private String userName;
    private int age;
    public Profile userProfile;
    private List<User> friends;
    private List<friendRequest> friendRequests;
    private List<Post> postsMade;
    private List<Post> postsReceived;
    private List<Notification> notificationsReceived;
    public User(String email, String password, String userName, int age, Profile userProfile) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.age = age;
        this.userProfile = userProfile;
        postsMade = new ArrayList<Post>();
        postsReceived = new ArrayList<Post>();
        notificationsReceived = new ArrayList<Notification>();
        friendRequests = new ArrayList<friendRequest>();
        friends = new ArrayList<User>();
    }

    public User( Integer userID ,String email, String password, String userName, int age, Profile userProfile) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.age = age;
        this.userProfile = userProfile;
        postsMade = new ArrayList<Post>();
        postsReceived = new ArrayList<Post>();
        notificationsReceived = new ArrayList<Notification>();
        friendRequests = new ArrayList<friendRequest>();
        friends = new ArrayList<User>();
    }



    public User(Integer userID, String email, String password, String userName, int age, Profile userProfile, List<User> friends, List<friendRequest> friendRequests, List<Post> postsMade, List<Post> postsReceived, List<Notification> notificationsReceived) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.age = age;
        this.userProfile = userProfile;
        this.friends = friends;
        this.friendRequests = friendRequests;
        this.postsMade = postsMade;
        this.postsReceived = postsReceived;
        this.notificationsReceived = notificationsReceived;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Profile userProfile) {
        this.userProfile = userProfile;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<friendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<friendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<Post> getPostsMade() {
        return postsMade;
    }

    public void setPostsMade(List<Post> postsMade) {
        this.postsMade = postsMade;
    }

    public List<Post> getPostsReceived() {
        return postsReceived;
    }

    public void setPostsReceived(List<Post> postsReceived) {
        this.postsReceived = postsReceived;
    }

    public List<Notification> getNotificationsReceived() {
        return notificationsReceived;
    }

    public void setNotificationsReceived(List<Notification> notificationsReceived) {
        this.notificationsReceived = notificationsReceived;
    }

    public void addFriend(User newFriend){
        this.friends.add(newFriend);
    }

    public void removeFriend(User removedFriend){
        this.friends.remove(removedFriend);
    }

    public void MakePost(Post newPost){
        this.postsMade.add(newPost);
    }

    public void removePost(Post remPost){
        if(this.postsMade.contains(remPost)) {
            this.postsMade.remove(remPost);
        } else{
            System.out.println("Post does not exist");
        }
    }

}
