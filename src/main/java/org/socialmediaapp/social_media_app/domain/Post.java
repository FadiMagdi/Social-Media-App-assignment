package org.socialmediaapp.social_media_app.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    public User postMaker;
    public Date postDate;
    public String image_path;
    private Integer postID;
    public List<Like> postLikes;
    public List<Comment> postComments;
    public Post(User postMaker, Date postDate, String image_path) {
        this.postMaker = postMaker;
        this.postDate = postDate;
        this.image_path = image_path;
        postLikes = new ArrayList<Like>();
        postComments = new ArrayList<Comment>();
    }

    public Post(User postMaker, Date postDate, String image_path, Integer postID, List<Like> postLikes, List<Comment> postComments) {
        this.postMaker = postMaker;
        this.postDate = postDate;
        this.image_path = image_path;
        this.postID = postID;
        this.postLikes = postLikes;
        this.postComments = postComments;
    }

    public User getPostMaker() {
        return postMaker;
    }

    public void setPostMaker(User postMaker) {
        this.postMaker = postMaker;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public List<Like> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(List<Like> postLikes) {
        this.postLikes = postLikes;
    }

    public List<Comment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<Comment> postComments) {
        this.postComments = postComments;
    }

    public void addLike(Integer userID,String userName,Profile userProfile){
        userDTO liking = new userDTO(userID,userName,userProfile);
        this.postLikes.add(new Like(liking));
    }

    public void removeLike(Integer userID) {


        Like target = null;
        for (int i = 0; i < postLikes.size(); i++) {
            if (postLikes.get(i).userMadeLike.userID().equals(userID)) {
                target = postLikes.get(i);
                break;
            }


        }
        if ((target != null)) {
            this.postLikes.remove(target);
        } else {
            System.out.println("user did not like the post");
        }

    }


    public void addComment(Comment newComment){
        this.postComments.add(newComment);
    }


    public void removeComment(Integer commentID){

        Comment target = null;
        for(int i=0;i<this.postComments.size();i++){
           Comment currentComment = this.postComments.get(i);
            if(commentID.equals(currentComment.getCommentID())){
                target = currentComment;
            }
            break;
        }
        if(target != null){
            this.postComments.remove(target);
        }else{
            System.out.println("Comment does not exist");
        }
    }

}
