package org.socialmediaapp.social_media_app.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A social media post with likes and comments.
 */
public class Post {

    private int id;
    private UserDTO author;
    private String text;
    private String imagePath;
    private Date date;
    private String privacy;
    private List<Like> likes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public Post() {}

    public Post(UserDTO author, String text, String imagePath, Date date, String privacy) {
        this.author = author;
        this.text = text;
        this.imagePath = imagePath;
        this.date = date;
        this.privacy = privacy;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public UserDTO getAuthor() { return author; }
    public void setAuthor(UserDTO author) { this.author = author; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public List<Like> getLikes() { return likes; }
    public void setLikes(List<Like> likes) { this.likes = likes; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
