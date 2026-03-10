package com.ashcollege.service;


import com.ashcollege.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }

    public <T> void remove(Object o){
        sessionFactory.getCurrentSession().remove(o);
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz)
    {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }


    public BasicUser getUserByUsername(String username) {
        BasicUser user = getClientByUsername(username);
        if (user == null) {
            user = getProffesionalByUsername(username);
        }
        return user;
    }
    public ClientEntity getClientByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ClientEntity " + " WHERE username = :username ", ClientEntity.class)
                .setParameter("username", username)
                .uniqueResult();
    }
    public ProffesionalEntity getProffesionalByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ProffesionalEntity " + " WHERE username = :username ", ProffesionalEntity.class)
                .setParameter("username", username)
                .uniqueResult();
    }



    public ClientEntity getUserByUsernameAndPassword(String username, String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ClientEntity  " +
                        "WHERE username = :username " +
                        "AND password = :password", ClientEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }

    public List<PostEntity> getPostsByClientId(int clientId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM PostEntity " +
                        "WHERE clientEntity.id = :clientId", PostEntity.class)
                .setParameter("clientId", clientId)
                .list();
    }

    public List<PostEntity> getAllPost() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM PostEntity", PostEntity.class)
                .list();
    }
    public List<CategoryEntity> getAllCategories() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM CategoryEntity", CategoryEntity.class)
                .list();
    }

    public PostEntity getPostByPostId(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM PostEntity " +
                        "WHERE id = :id", PostEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }
    public CategoryEntity getCategoryByCategoryId(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM CategoryEntity " +
                        "WHERE id = :id", CategoryEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    public ClientEntity getClientByToken(String token) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ClientEntity " +
                        "WHERE token = :token", ClientEntity.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    public ProffesionalEntity getProfessionalByToken(String token) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ProffesionalEntity " +
                        "WHERE token = :token", ProffesionalEntity.class)
                .setParameter("token", token)
                .uniqueResult();
    }


    public ProffesionalEntity getProffesionalByUsernameAndPassword(String username, String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ProffesionalEntity " +
                        "WHERE username = :username " +
                        "AND password = :password", ProffesionalEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }

    public BasicUser getUserByToken(String token) {
        BasicUser user = getClientByToken(token);
        if (user == null) {
            user = getProfessionalByToken(token);
        }
        return user;
    }

    public List<BidEntity> getBidsByProfessionalId(int professionalId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM BidEntity " +
                        "WHERE proffesionalEntity.id = :professionalId", BidEntity.class)
                .setParameter("professionalId", professionalId)
                .list();
    }

    public List<BidEntity> getProposalsByClientId(int clientId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM BidEntity bid " +
                                "WHERE bid.postEntity.clientEntity.id = :clientId", BidEntity.class)
                .setParameter("clientId", clientId)
                .list();
    }

    public List<MessageEntity> getConversation (int bidId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM MessageEntity msg " +
                                "WHERE msg.bidEntity.id = :bidId " +
                                "ORDER BY msg.id DESC ",
                        MessageEntity.class)
                .setParameter("bidId", bidId)
                .setMaxResults(10)
                .list();
    }





}