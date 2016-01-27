package com.semjournals.data.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory SESSION_FACTORY;
    public static final ThreadLocal MAP = new ThreadLocal();

    private HibernateUtil() { }

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try {
            // Creating the SessionFactory from the hibernate.cfg.xml config file
            LOG.debug("HibernateUtil.static - loading config");
            SESSION_FACTORY = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            LOG.debug("HibernateUtil.static - config fully loaded");
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );

            LOG.error("Initial SessionFactory creation failed.", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static Session getCurrentSession() throws HibernateException {
        Session s = (Session)MAP.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = SESSION_FACTORY.openSession();
            MAP.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session)MAP.get();
        MAP.set(null);
        if (s != null) {
            s.close();
        }
    }
}