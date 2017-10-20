package com.dao;

import com.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Sang on 15/11/2.
 */
@Repository
public class UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public boolean login(User user){
        Query query=getCurrentSession().createQuery("from User u where u.name=? and u.password=?");
        query.setString(0,user.getName());
        query.setString(1,user.getPassword());
        List<User> result=query.list();
        if(result.size()>0){
            return true;
        }
        return false;
    }
}
