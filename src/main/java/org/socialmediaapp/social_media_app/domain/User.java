package org.socialmediaapp.social_media_app.domain;

/**
 * Full user entity with credentials.
 */
public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private int age;
    private Profile profile;

    public User() {}

    public User(int id, String name, String email, String password, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
}
