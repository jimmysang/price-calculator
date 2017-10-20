package com.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by Sang on 15/6/27.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory=null;

    private static Session session = null;
    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null){
            Configuration config = new Configuration().configure();
            ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(config.getProperties()).build();
            sessionFactory = config.buildSessionFactory(sr);
        }
        return sessionFactory;
    }

    public static Session getSession(){
        if(session==null){
            return getSessionFactory().openSession();
        }else {
            return getSessionFactory().getCurrentSession();
        }
    }
}
