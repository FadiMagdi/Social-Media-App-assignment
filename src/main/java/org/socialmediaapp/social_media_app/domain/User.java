package org.socialmediaapp.social_media_app.domain;

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
            System.out.println("Post do not exist");
        }
    }

}
