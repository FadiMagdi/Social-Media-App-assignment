package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.PostManagementDao;
import org.socialmediaapp.social_media_app.domain.Comment;
import org.socialmediaapp.social_media_app.domain.Like;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.userDTO;

import java.util.Date;
import java.util.List;

public class PostManagementService {

    private PostManagementDao postManagementDao;




    public PostManagementService(PostManagementDao postManagementDao) {
        this.postManagementDao = postManagementDao;
    }


    // Like

    public boolean addLiketoPost(Integer PostID,Integer userID, String userName){

        Like newLike = new Like(userName,userID);

        return this.postManagementDao.addLikeToPost(newLike , PostID);
    }


    public List<Like> getPostLikes(Integer PostID){
        return this.postManagementDao.getPostLikes(PostID);
    }

    //comments
    public boolean addCommentToPost(Integer PostID, Integer userMadeCommentID, String userMadeCommentName, String commentText){
        Comment newComment = new Comment(userMadeCommentName,commentText, new Date(), userMadeCommentID);
        return this.postManagementDao.addCommentToPost(newComment, PostID);
    }

    public List<Comment> getPostComments(Integer PostID){
        return this.postManagementDao.getPostComments(PostID);
    }

    //Posts

    public boolean addPost(Integer PostMakerID , String PostMakerName ,String postText , String post_Image_Path , String post_Privacy){

        Post newPost = new Post(PostMakerID,PostMakerName,new Date(),post_Image_Path,postText,post_Privacy);

        return this.postManagementDao.addPost(newPost);
    }
    public List<Post> getPostsByUserID (Integer userID){
        List<Post> postsMadeByUser =  this.postManagementDao.getPostsByUserID(userID);

        for(int i=0 ; i<postsMadeByUser.size() ; i++){
            Post currentPost = postsMadeByUser.get(i);

            List<Like> postLikes = this.getPostLikes(currentPost.getPostID());
            List<Comment> postComments = this.getPostComments(currentPost.getPostID());


            currentPost.setPostLikes(postLikes);
            currentPost.setPostComments(postComments);

        }

        return postsMadeByUser;
    }

    public List<Post> getPostsForNewsFeed(Integer userID){
        List<Post> postsInNewsFeed = this.postManagementDao.getPostsForNewsFeed(userID);

        for(int i=0 ; i<postsInNewsFeed.size() ; i++){
            Post currentPost = postsInNewsFeed.get(i);

            List<Like> postLikes = this.getPostLikes(currentPost.getPostID());
            List<Comment> postComments = this.getPostComments(currentPost.getPostID());

            currentPost.setPostLikes(postLikes);
            currentPost.setPostComments(postComments);
        }

        return postsInNewsFeed;
    }

    public List<Post> getPostsByUserIDAndPrivacy (Integer userID,String privacy){
        return this.postManagementDao.getPostsByUserIDAndPrivacy(userID, privacy);
    }
}
