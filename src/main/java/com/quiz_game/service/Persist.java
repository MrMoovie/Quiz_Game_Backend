package com.quiz_game.service;


import com.quiz_game.entities.*;
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

    public <T> void remove(Object o) {
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

    public <T> List<T> loadList(Class<T> clazz) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }

//  >> NOT RELEVANT TO OUR PROJECT << //
//    public BasicUser getUserByUsername(String username) {
//        BasicUser user = getClientByUsername(username);
//        if (user == null) {
//            user = getProffesionalByUsername(username);
//        }
//        return user;
//    }
//    public ClientEntity getClientByUsername(String username) {
//        return this.sessionFactory.getCurrentSession()
//                .createQuery("FROM ClientEntity " + " WHERE username = :username ", ClientEntity.class)
//                .setParameter("username", username)
//                .uniqueResult();
//    }
//    public ProffesionalEntity getProffesionalByUsername(String username) {
//        return this.sessionFactory.getCurrentSession()
//                .createQuery("FROM ProffesionalEntity " + " WHERE username = :username ", ProffesionalEntity.class)
//                .setParameter("username", username)
//                .uniqueResult();
//    }


    //sign_in and sign_up
    public StudentEntity getStudentByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM StudentEntity " + " WHERE username = :username ", StudentEntity.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    public TeacherEntity getTeacherByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM TeacherEntity " + " WHERE username = :username ", TeacherEntity.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    public BasicUser getUserByUsername(String username) {
        BasicUser user = getStudentByUsername(username);
        if (user == null) {
            user = getTeacherByUsername(username);
        }
        return user;
    }


    public StudentEntity getStudentByUsernameAndPassword(String username, String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM StudentEntity  " +
                        "WHERE username = :username " +
                        "AND password = :password", StudentEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }
    public TeacherEntity getTeacherByUsernameAndPassword(String username, String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM TeacherEntity  " +
                        "WHERE username = :username " +
                        "AND password = :password", TeacherEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }

    public BasicUser getUserByUsernameAndPassword(String username, String password) {
        BasicUser user = getStudentByUsernameAndPassword(username, password);
        if (user == null) {
            user = getTeacherByUsernameAndPassword(username, password);
        }
        return user;
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
    public List<StudentEntity> getAllStudentsByRaceID(int raceId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("SELECT r.student FROM TrackEntity r " +
                        "WHERE r.race.id = :raceId", StudentEntity.class)
                .setParameter("raceId", raceId)
                .getResultList();
    }

    public boolean isTeacherHostingRace(TeacherEntity teacherEntity, int raceId) {
        Long count = this.sessionFactory.getCurrentSession()
                .createQuery("SELECT count(r) FROM RaceEntity r " +
                        "WHERE r.id = :raceId " +
                        "AND r.teacher.id = :teacherId", Long.class)
                .setParameter("raceId", raceId)
                .setParameter("teacherId", teacherEntity.getId())
                .uniqueResult();

        return count != null && count > 0;
    }

    public boolean isStudentInRace(StudentEntity studentEntity, int raceId) {
        Long count = this.sessionFactory.getCurrentSession()
                .createQuery("SELECT count(r) FROM RaceEntity r " +
                        "WHERE r.id = :raceId " +
                        "AND r.student.id = :studentId", Long.class)
                .setParameter("raceId", raceId)
                .setParameter("studentId", studentEntity.getId())
                .uniqueResult();

        return count != null && count > 0;
    }

    public RaceEntity getRaceByRaceId(int raceId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RaceEntity r WHERE r.id = :raceId", RaceEntity.class)
                .setParameter("raceId", raceId)
                .uniqueResult();
    }
    public ProffesionalEntity getProfessionalByToken(String token) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ProffesionalEntity " +
                        "WHERE token = :token", ProffesionalEntity.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    public TeacherEntity getTeacherByToken(String token) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM TeacherEntity " +
                        "WHERE token = :token", TeacherEntity.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    public StudentEntity getStudentByToken(String token) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM StudentEntity " +
                        "WHERE token = :token", StudentEntity.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    public RaceEntity getRaceByEntryCode(String entryCode) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RaceEntity " +
                        "WHERE entryCode = :entryCode", RaceEntity.class)
                .setParameter("entryCode", entryCode)
                .uniqueResult();
    }

    public List<RaceEntity> getAllRaces() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RaceEntity", RaceEntity.class)
                .list();
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

    public List<MessageEntity> getConversation(int bidId) {
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