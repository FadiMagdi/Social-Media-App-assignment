package org.socialmediaapp.social_media_app.domain;

import java.util.Date;

/**
 * A comment on a post.
 */
public class Comment {

    private int id;
    private UserDTO author;
    private String text;
    private Date date;

    public Comment() {}

    public Comment(UserDTO author, String text, Date date) {
        this.author = author;
        this.text = text;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public UserDTO getAuthor() { return author; }
    public void setAuthor(UserDTO author) { this.author = author; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
