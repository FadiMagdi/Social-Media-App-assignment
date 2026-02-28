package org.socialmediaapp.social_media_app.service;

import org.socialmediaapp.social_media_app.dao.UserDao;
import org.socialmediaapp.social_media_app.domain.userDTO;

public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public userDTO getUserDTOByID(Integer userID){
        return this.userDao.getUserDTOByID(userID);
    }


    public boolean createUser(String email, String password, String userName, int age,String bio,String image_path){
        return this.userDao.createUser( email, password, userName,  age, bio,image_path);
    }



}
