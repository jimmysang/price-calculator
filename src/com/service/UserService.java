package com.service;

import com.dao.UserDao;
import com.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Sang on 15/11/2.
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public boolean login(User user){
        return userDao.login(user);
    }
}
