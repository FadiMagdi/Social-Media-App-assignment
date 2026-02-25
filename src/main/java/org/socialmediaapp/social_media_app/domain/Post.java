package org.socialmediaapp.social_media_app.domain;

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
    }

    public void addLike(User liking){
        this.postLikes.add(new Like(liking));
    }

    public void removeLike(User removing) {


        Like target = null;
        for (int i = 0; i < postLikes.size(); i++) {
            if (postLikes.get(i).userMadeLike.equals(removing)) {
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
