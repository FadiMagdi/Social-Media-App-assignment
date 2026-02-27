package org.socialmediaapp.social_media_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.socialmediaapp.social_media_app.HelloApplication;
import org.socialmediaapp.social_media_app.dao.PostManagementDao;
import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.database.DatabaseConnection;
import org.socialmediaapp.social_media_app.domain.Post;
import org.socialmediaapp.social_media_app.domain.userDTO;
import org.socialmediaapp.social_media_app.util.SceneManager;
import org.socialmediaapp.social_media_app.util.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private VBox usersResultsContainer;
    @FXML private VBox postsResultsContainer;
    @FXML private Label usersHeader;
    @FXML private Label postsHeader;
    @FXML private Label noResultsLabel;

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;
        performSearch(query);
    }

    /**
     * Called from MainController when searching from the top bar.
     */
    public void performSearch(String query) {
        searchField.setText(query);
        usersResultsContainer.getChildren().clear();
        postsResultsContainer.getChildren().clear();
        noResultsLabel.setVisible(false);

        boolean foundResults = false;

        try {
            Connection conn = DatabaseConnection.getDBConnection();
            UserDao userDao = new UserDao(conn);

            // Search Users
            List<userDTO> users = searchUsers(conn, userDao, query);
            if (!users.isEmpty()) {
                foundResults = true;
                usersHeader.setVisible(true);
                usersHeader.setManaged(true);
                for (userDTO user : users) {
                    usersResultsContainer.getChildren().add(createUserResultCard(user));
                }
            } else {
                usersHeader.setVisible(false);
                usersHeader.setManaged(false);
            }

            // Search Posts
            PostManagementDao postDao = new PostManagementDao(conn, userDao);
            List<Post> posts = searchPosts(conn, userDao, query);
            if (!posts.isEmpty()) {
                foundResults = true;
                postsHeader.setVisible(true);
                postsHeader.setManaged(true);
                for (Post post : posts) {
                    try {
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("post-card.fxml"));
                        Parent postCard = loader.load();
                        PostCardController controller = loader.getController();
                        controller.setPost(post);
                        postsResultsContainer.getChildren().add(postCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                postsHeader.setVisible(false);
                postsHeader.setManaged(false);
            }

            if (!foundResults) {
                noResultsLabel.setText("No results found for \"" + query + "\"");
                noResultsLabel.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            noResultsLabel.setText("Search failed. Please try again.");
            noResultsLabel.setVisible(true);
        }
    }

    private List<userDTO> searchUsers(Connection conn, UserDao userDao, String query) {
        List<userDTO> results = new ArrayList<>();
        String sql = "SELECT id, name FROM app_user WHERE name LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userDTO user = userDao.getUserDTOByID(rs.getInt("id"));
                if (user != null) {
                    results.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<Post> searchPosts(Connection conn, UserDao userDao, String query) {
        List<Post> results = new ArrayList<>();
        String sql = "SELECT id, user_id, post_text, post_image_path, post_date, privacy FROM posts WHERE post_text LIKE ? AND privacy = 'Public'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            PostManagementDao postDao = new PostManagementDao(conn, userDao);
            while (rs.next()) {
                userDTO postMaker = userDao.getUserDTOByID(rs.getInt("user_id"));
                List<org.socialmediaapp.social_media_app.domain.Like> likes = postDao.getPostLikes(rs.getInt("id"));
                List<org.socialmediaapp.social_media_app.domain.Comment> comments = postDao.getPostComments(rs.getInt("id"));

                Post post = new Post(
                        postMaker,
                        rs.getDate("post_date"),
                        rs.getString("post_image_path"),
                        rs.getInt("id"),
                        likes,
                        comments,
                        rs.getString("post_text"),
                        rs.getString("privacy")
                );
                results.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private HBox createUserResultCard(userDTO user) {
        HBox card = new HBox(12);
        card.getStyleClass().add("friend-card");
        card.setAlignment(Pos.CENTER_LEFT);

        // Avatar
        VBox avatar = new VBox();
        avatar.setAlignment(Pos.CENTER);
        avatar.setMinSize(40, 40);
        avatar.setMaxSize(40, 40);
        avatar.setStyle("-fx-background-color: #1877F2; -fx-background-radius: 20;");
        Label initial = new Label(user.userName().substring(0, 1).toUpperCase());
        initial.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        avatar.getChildren().add(initial);

        // Name
        Label nameLabel = new Label(user.userName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // View Profile
        Button viewBtn = new Button("View Profile");
        viewBtn.getStyleClass().add("btn-outline");
        viewBtn.setOnAction(e -> {
            ProfileController controller = SceneManager.getInstance()
                    .loadContentAndGetController("profile-view.fxml");
            if (controller != null) {
                controller.loadProfile(user.userID());
            }
        });

        // Add Friend (if not self)
        Integer currentUserId = SessionManager.getInstance().getCurrentUserID();
        if (!user.userID().equals(currentUserId)) {
            Button addBtn = new Button("Add Friend");
            addBtn.getStyleClass().add("btn-secondary");
            addBtn.setStyle("-fx-font-size: 12px; -fx-padding: 6 12;");
            addBtn.setOnAction(e -> {
                try {
                    Connection conn = DatabaseConnection.getDBConnection();
                    UserDao userDao = new UserDao(conn);
                    org.socialmediaapp.social_media_app.dao.FriendSystemDao friendDao =
                            new org.socialmediaapp.social_media_app.dao.FriendSystemDao(conn, userDao);

                    userDTO currentUserDTO = userDao.getUserDTOByID(currentUserId);
                    org.socialmediaapp.social_media_app.domain.friendRequest request =
                            new org.socialmediaapp.social_media_app.domain.friendRequest(
                                    currentUserDTO,
                                    new java.sql.Date(System.currentTimeMillis()),
                                    user.userID()
                            );

                    boolean success = friendDao.createFriendRequest(request);
                    if (success) {
                        addBtn.setText("Sent");
                        addBtn.setDisable(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            card.getChildren().addAll(avatar, nameLabel, spacer, viewBtn, addBtn);
        } else {
            card.getChildren().addAll(avatar, nameLabel, spacer, viewBtn);
        }

        return card;
    }
}
