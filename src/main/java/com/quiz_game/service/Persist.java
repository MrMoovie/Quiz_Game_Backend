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
import java.util.Random;

import static com.quiz_game.utils.Constants.RACE_STATUS_FINISHED;


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

    public QuestionTemplateEntity getRandomTemplate(String level) {
        return (QuestionTemplateEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM QuestionTemplateEntity q WHERE q.level = :level ORDER BY FUNCTION('RAND')")
                .setParameter("level", level)
                .setMaxResults(1)
                .uniqueResult();
    }

    public ObjectEntity getRandomObject() {
        return (ObjectEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM ObjectEntity ORDER BY FUNCTION('RAND')")
                .setMaxResults(1)
                .uniqueResult();
    }

    public NameEntity getRandomName() {
        return (NameEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM NameEntity ORDER BY FUNCTION('RAND')")
                .setMaxResults(1)
                .uniqueResult();
    }

    public ActionEntity getRandomAction() {
        return (ActionEntity) this.sessionFactory.getCurrentSession()
                .createQuery("FROM ActionEntity ORDER BY FUNCTION('RAND')")
                .setMaxResults(1)
                .uniqueResult();
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

    public boolean isStudentInRace(StudentEntity studentEntity, int trackId) {
        Long count = this.sessionFactory.getCurrentSession()
                .createQuery("SELECT count(r) FROM TrackEntity r " +
                        "WHERE r.id = :trackId " +
                        "AND r.student.id = :studentId", Long.class)
                .setParameter("trackId", trackId)
                .setParameter("studentId", studentEntity.getId())
                .uniqueResult();

        return count != null && count > 0;
    }
    public boolean isStudentInSpecificRace(StudentEntity studentEntity, int raceId) {
        Long count = this.sessionFactory.getCurrentSession()
                .createQuery("SELECT count(t) FROM TrackEntity t " +
                        "WHERE t.race.id = :raceId " +
                        "AND t.student.id = :studentId", Long.class)
                .setParameter("raceId", raceId)
                .setParameter("studentId", studentEntity.getId())
                .uniqueResult();

        return count != null && count > 0;
    }

    public void removeUnfinishedTracksForStudent(StudentEntity student) {
        Session session = this.sessionFactory.getCurrentSession();

        List<TrackEntity> activeTracks = session
                .createQuery("SELECT t FROM TrackEntity t WHERE t.student = :student AND t.race.status != :finishedStatus", TrackEntity.class)
                .setParameter("student", student)
                .setParameter("finishedStatus", RACE_STATUS_FINISHED)
                .getResultList();

        for (TrackEntity oldTrack : activeTracks) {
            // 1. Delete questions (HQL is perfect here because Track doesn't hold a list of questions in Java)
            session.createQuery("DELETE FROM QuestionEntity q WHERE q.track.id = :trackId")
                    .setParameter("trackId", oldTrack.getId())
                    .executeUpdate();

            // 2. Break the link to the Race so Hibernate doesn't resurrect it!
            RaceEntity oldRace = oldTrack.getRace();
            if (oldRace != null) {
                if (oldRace.getTracks() != null) {
                    oldRace.getTracks().remove(oldTrack); // Removes it from the Java memory list
                }
                oldRace.setCapacity(Math.max(0, oldRace.getCapacity() - 1));
                session.saveOrUpdate(oldRace);
            }

            // 3. Break the link to the Student so Hibernate doesn't resurrect it!
            if (student.getGameHistory() != null) {
                student.getGameHistory().remove(oldTrack); // Removes it from the Java memory list
            }

            // 4. Now that the Java lists are completely clear, we safely delete the track object
            session.remove(oldTrack);

            System.out.println("Successfully destroyed abandoned track ID: " + oldTrack.getId());
        }
    }

    public QuestionTemplateEntity getQuestionTemplateByQuestionId(int questionId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM QuestionTemplateEntity q WHERE q.id = :questionId", QuestionTemplateEntity.class)
                .setParameter("questionId", questionId)
                .uniqueResult();
    }

    public RaceEntity getRaceByRaceId(int raceId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RaceEntity r WHERE r.id = :raceId", RaceEntity.class)
                .setParameter("raceId", raceId)
                .uniqueResult();
    }

    public TrackEntity getTrackByTrackId(int trackId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM TrackEntity t WHERE t.id = :trackId", TrackEntity.class)
                .setParameter("trackId", trackId)
                .uniqueResult();
    }
    public TrackEntity getTrackByStudentToken(String studentToken) {
        return this.sessionFactory.getCurrentSession()
                .createQuery(
                        "SELECT t FROM TrackEntity t " +
                                "JOIN t.student s " + // מניח שיש קשר ב-Entity בין Track ל-Student
                                "WHERE s.token = :token", TrackEntity.class)
                .setParameter("token", studentToken)
                .uniqueResult();
    }

    public QuestionEntity getQuestionById(int questionId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM QuestionEntity q WHERE q.id = :questionId",  QuestionEntity.class)
                .setParameter("questionId", questionId)
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

    public List<RaceEntity> getRacesByTeacherId(int teacherId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RaceEntity r WHERE r.teacher.id = :teacherId", RaceEntity.class)
                .setParameter("teacherId", teacherId)
                .list();
    }

    public boolean isAnyRaceOpenForTeacher(TeacherEntity teacher) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("SELECT 1 FROM RaceEntity r WHERE r.status = 1 AND r.teacher = :teacher", Integer.class)
                .setParameter("teacher", teacher)
                .setMaxResults(1)
                .uniqueResult() != null;
    }


    public boolean isStudentInAnyNonFinishedRace(StudentEntity student) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("SELECT 1 FROM TrackEntity t WHERE t.student = :student AND t.race.status != :finishedStatus", Integer.class)
                .setParameter("student", student)
                .setParameter("finishedStatus", RACE_STATUS_FINISHED)
                .setMaxResults(1)
                .uniqueResult() != null;
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
        BasicUser user = getStudentByToken(token);
        if (user == null) {
            user = getTeacherByToken(token);
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

    public RaceEntity getRaceByTeacherId(int teacherId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM RaceEntity " + " WHERE teacher_id = :teacherId ", RaceEntity.class)
                .setParameter("teacherId", teacherId)
                .uniqueResult();
    }




}