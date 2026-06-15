package com.quiz_game.service;


import com.quiz_game.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.quiz_game.utils.Constants.RACE_STATUS_FINISHED;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {


    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public void deleteRaceAndComponents(int raceId) {
        Session session = this.sessionFactory.getCurrentSession();

        // 1. מחיקת כל השאלות ששייכות למסלולים של המירוץ הזה (שימוש ב-HQL נקי)
        session.createQuery("DELETE FROM QuestionEntity q WHERE q.track.id IN (SELECT t.id FROM TrackEntity t WHERE t.race.id = :raceId)")
                .setParameter("raceId", raceId)
                .executeUpdate();

        // 2. מחיקת כל המסלולים (tracks) ששייכים למירוץ הזה
        session.createQuery("DELETE FROM TrackEntity t WHERE t.race.id = :raceId")
                .setParameter("raceId", raceId)
                .executeUpdate();

        // 3. שליפת המירוץ עצמו כדי לנתק אותו מרשימת המורה בזיכרון לפני המחיקה
        RaceEntity race = session.get(RaceEntity.class, raceId);
        if (race != null) {
            TeacherEntity teacher = race.getTeacher();
            // אם יש לך רשימת מירוצים בתוך TeacherEntity, ננתק את המירוץ ממנה
            if (teacher != null && teacher.getRaces() != null) {
                teacher.getRaces().remove(race);
            }

            // 4. מחיקת המירוץ עצמו בצורה בטוחה
            session.remove(race);
        }
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

            //System.out.println("Successfully destroyed abandoned track ID: " + oldTrack.getId());
        }
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
                                "JOIN t.student s " +
                                "WHERE s.token = :token " +
                                "AND t.race.status != :finishedStatus", TrackEntity.class)
                .setParameter("token", studentToken)
                .setParameter("finishedStatus", RACE_STATUS_FINISHED)
                .uniqueResult();
    }

    public QuestionEntity getQuestionById(int questionId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM QuestionEntity q WHERE q.id = :questionId", QuestionEntity.class)
                .setParameter("questionId", questionId)
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


    public boolean isStudentInAnyNonFinishedRace(StudentEntity student) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("SELECT 1 FROM TrackEntity t WHERE t.student = :student AND t.race.status != :finishedStatus", Integer.class)
                .setParameter("student", student)
                .setParameter("finishedStatus", RACE_STATUS_FINISHED)
                .setMaxResults(1)
                .uniqueResult() != null;
    }

    public BasicUser getUserByToken(String token) {
        BasicUser user = getStudentByToken(token);
        if (user == null) {
            user = getTeacherByToken(token);
        }
        return user;
    }

}