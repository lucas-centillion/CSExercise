package com.semjournals.data.dao;

import com.semjournals.data.util.HibernateUtil;
import com.semjournals.model.AbstractPersistentObject;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GenericDAO<T extends AbstractPersistentObject> implements DAO<T> {
    private SessionFactory sessionFactory;
    private Class<T> type;

    public GenericDAO(Class<T> type) {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.type = type;
    }

    @Override
    public T create(T persistentObject) {
        checkNotNull(persistentObject);
        checkNotNull(persistentObject.getId());

        HibernateUtil.closeSession();

        T createdObject;
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            Serializable id = session.save(persistentObject);
            session.getTransaction().commit();

            createdObject = session.get(type, id);

        }

        return createdObject;
    }

    @Override
    public T get(String id) {
        checkNotNull(id);

        Session session = HibernateUtil.getCurrentSession();
        T value = session.get(type, id);

        if (value instanceof HibernateProxy) {
            Hibernate.initialize(value);
            value = (T) ((HibernateProxy) value).getHibernateLazyInitializer().getImplementation();
        }

        return value;
    }

    @Override
    public T get(String key, Object value) {
        checkNotNull(key);
        checkNotNull(value);

        Session session = HibernateUtil.getCurrentSession();
        Criteria criteria = session.createCriteria(type);
        T returnObject = (T) criteria.add(Restrictions.eq(key, value)).uniqueResult();

        if (returnObject instanceof HibernateProxy) {
            Hibernate.initialize(returnObject);
            returnObject = (T) ((HibernateProxy) returnObject).getHibernateLazyInitializer().getImplementation();
        }

        return returnObject;
    }

    @Override
    public List<T> list() {
        return list(false);
    }

    public List<T> listActive() {
        return list(true);
    }

    private List<T> list(boolean showActiveOnly) {
        Session session = HibernateUtil.getCurrentSession();
        Criteria criteria = session.createCriteria(type);

        if (showActiveOnly){
            criteria.add(Restrictions.eq("active", true));
        }

        return criteria.list();
    }

    @Override
    public List<T> list(int offset, int maxSize) {
        return list(offset, maxSize, false);
    }

    public List<T> listActive(int offset, int maxSize) {
        return list(offset, maxSize, true);
    }

    private List<T> list(int offset, int maxSize, boolean active) {
        checkArgument(offset >= 0);
        checkArgument(maxSize > 0);

        Session session = HibernateUtil.getCurrentSession();
        Criteria criteria = session.createCriteria(type).setFirstResult(offset).setMaxResults(maxSize);

        if (active){
            criteria.add(Restrictions.eq("active", true));
        }

        return criteria.list();
    }

    @Override
    public void update(T persistentObject) {
        checkNotNull(persistentObject);
        checkNotNull(persistentObject.getId());

        HibernateUtil.closeSession();

        try (Session session = sessionFactory.openSession()) {
            Transaction t = session.beginTransaction();
            session.update(persistentObject);
            t.commit();
        }
    }

    @Override
    public void delete(T persistentObject) {
        checkNotNull(persistentObject);
        checkNotNull(persistentObject.getId());

        HibernateUtil.closeSession();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(persistentObject);
            session.getTransaction().commit();
        }
    }
}
