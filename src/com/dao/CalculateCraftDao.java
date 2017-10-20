package com.dao;

import com.model.Craft;
import com.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sang on 15/10/21.
 */
@Repository
public class CalculateCraftDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Craft findCraft(String name,String key1,String key2,String key3){
        Session session = sessionFactory.getCurrentSession();
//        Session session = HibernateUtil.getSession();
        DetachedCriteria dc= DetachedCriteria.forClass(Craft.class);
        dc.add(Restrictions.eq("name",name));
        if(key1!=null){
            dc.add(Restrictions.eq("key1", key1));
        }
        if(key2!=null){
            dc.add(Restrictions.eq("key2", key2));
        }
        if(key3!=null){
            dc.add(Restrictions.eq("key3",key3));
        }
        Criteria c=dc.getExecutableCriteria(session);
        List<Craft> craftList=c.list();
        return craftList.get(0);
    };

    public Craft findCraft(String name,String key1){
        return findCraft(name, key1, null, null);
    };

    public Craft findCraft(String name){
        return findCraft(name, null, null, null);
    };

    public Craft findCraft(String name,String key1,String key2){
        return findCraft(name,key1,key2,null);
    };

}
