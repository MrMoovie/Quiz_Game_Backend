package com.quiz_game.controllers;

import com.quiz_game.entities.ClassroomEntity;
import com.quiz_game.entities.SessionEntity;
import com.quiz_game.entities.StudentEntity;
import com.quiz_game.entities.TeacherEntity;
import com.quiz_game.responses.CreateSessionResponse;
import com.quiz_game.responses.JoinSessionResponse;
import com.quiz_game.service.Persist;
import com.quiz_game.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import static com.quiz_game.utils.Errors.*;

@RestController
public class PreGameController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/create-session")
    public CreateSessionResponse createSession(String token, Integer classroomId) {
        TeacherEntity teacher = persist.getTeacherByToken(token);
        if (teacher != null) {
            if (classroomId != null && classroomId > 0) {
                ClassroomEntity classroom = persist.loadObject(ClassroomEntity.class, classroomId);
                if (classroom != null) {
                    String entryCode = GeneralUtils.generateOtp();
                    SessionEntity session = new SessionEntity();
                    session.setTeacher(teacher);
                    session.setEntryCode(entryCode);
                    session.setOpen(true);
                    session.setStatus(0);
                    persist.save(session);
                    return new CreateSessionResponse(true, null, session.getId(), entryCode);
                } else {
                    return new CreateSessionResponse(false, ERROR_MISSING_VALUES);
                }
            } else {
                return new CreateSessionResponse(false, ERROR_MISSING_VALUES);
            }
        } else {
            return new CreateSessionResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/join-session")
    public JoinSessionResponse joinSession(String token, String entryCode) {
        StudentEntity student = persist.getStudentByToken(token);
        if (student != null) {
            if (entryCode != null && !entryCode.trim().isEmpty()) {
                SessionEntity session = persist.getSessionByEntryCode(entryCode.trim());
                if (session != null && session.isOpen()) {
                    return new JoinSessionResponse(true, null, session.getId());
                } else {
                    return new JoinSessionResponse(false, ERROR_MISSING_VALUES);
                }
            } else {
                return new JoinSessionResponse(false, ERROR_MISSING_VALUES);
            }
        } else {
            return new JoinSessionResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }
}
