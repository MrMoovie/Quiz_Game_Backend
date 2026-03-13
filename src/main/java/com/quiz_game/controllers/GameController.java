package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.util.List;

import static com.quiz_game.utils.Errors.*;

@RestController
public class GameController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }
    public BasicResponse teacherAndRaceBadResponse(String teacherToken, int raceId) {
        TeacherEntity teacherEntity = persist.getTeacherByToken(teacherToken);
        if (teacherEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isTeacherHostingRace(teacherEntity, raceId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }
        return null; // true - teacher created race.
    }

    @RequestMapping("/get-all-students-in-race")
    public BasicResponse getAllStudents(String teacherToken,int raceId) {
        BasicResponse badResponse = teacherAndRaceBadResponse(teacherToken, raceId);
        if (badResponse != null) {
            return badResponse;
        }
        List<StudentEntity> studenList = persist.getAllStudentsByRaceID(raceId);
        return new RaceStudentsResponse(true,null, studenList);
    }


    public BasicResponse setStatus(String teacherToken, int raceId, Integer status) {
        BasicResponse badResponse = teacherAndRaceBadResponse(teacherToken, raceId);
        if (badResponse != null) {
            return badResponse;
        }
        RaceEntity raceEntity = persist.getRaceByRaceId(raceId);
        if (status != null) {
            // need to complete: {
            raceEntity.setStatus(status);
            persist.save(raceEntity);
            // }
        }
        return new StatusResponse(true, raceEntity.getStatus(), null);
    }

    @RequestMapping("/send-new-status")
    public BasicResponse sendNewStatus(String teacherToken, int raceId, Integer status) {
        return setStatus(teacherToken, raceId, status);
    }

    @RequestMapping("/get-status")
    public BasicResponse getStatus(String teacherToken, int raceId) {
        return setStatus(teacherToken,raceId,null);
    }

    public BasicResponse studentAndRaceBadResponse(String studentToken, int raceId) {
        StudentEntity studentEntity = persist.getStudentByToken(studentToken);
        if (studentEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isStudentInRace(studentEntity, raceId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
        }
        return null; // true - student is in race.
    }
    @RequestMapping("/add-question")
    public BasicResponse addQuestion(String studentToken, int raceId, int pathChoice) {
        BasicResponse badResponse = studentAndRaceBadResponse(studentToken, raceId);
        if (badResponse != null) {
            return badResponse;
        }
        if (pathChoice < 0 || pathChoice > 2) { // pathChoice = 0 (normal) || 1 (highway) || 2 (dirt road)
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        QuestionTemplateEntity questionTemplate = new QuestionTemplateEntity();
        questionTemplate.setDeleted(false);
        //  questionTemplate.setTemplate("");
  //      questionTemplate.setMaxNumber(1000000);
      //  questionTemplate.setCreationDate();
     //   questionTemplate.setDifficultyLevel();
        persist.save(questionTemplate);
        return new QuestionTemplateResponse(true, questionTemplate, null);
    }

//    @RequestMapping("/get-question")
//    public BasicResponse getQuestion(String studentToken, int raceId) {
//        BasicResponse badResponse = studentAndRaceBadResponse(studentToken, raceId);
//        if (badResponse != null) {
//            return badResponse;
//        }
//
//        return new QuestionTemplateResponse(true, question, null);
//    }

}