package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Random;

import static com.quiz_game.utils.Errors.*;

@RestController
public class GameController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/get-all-students-in-race") //MAJOR SECURITY WARNING
    public BasicResponse getAllStudents(String teacherToken, int raceId) {
        TeacherEntity teacherEntity = persist.getTeacherByToken(teacherToken);
        if (teacherEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED); //ERROR_WRONG_CREDENTIALS
        }
        if (!persist.isTeacherHostingRace(teacherEntity, raceId)) { // Can do only getRaces, and check for null
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }
        List<StudentEntity> studenList = persist.getAllStudentsByRaceID(raceId);
        return new RaceStudentsResponse(true, null, studenList);
    }

    @RequestMapping("/getNewQuestion")
    public BasicResponse getNewQuestion(String studentToken, int raceId, int pathChoice) {
        StudentEntity studentEntity = persist.getStudentByToken(studentToken);
        if (studentEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isStudentInRace(studentEntity, raceId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
        }
        if (pathChoice < 0 || pathChoice > 2) { // pathChoice = 0 (normal)  || 1 (dirt road) || 2 (highway)
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        QuestionTemplateEntity questionTemplate = new QuestionTemplateEntity();
        questionTemplate.setDeleted(false);
        Random random = new Random();
        questionTemplate.setDifficultyLevel(String.valueOf(pathChoice));

        String object = persist.getRandomObjectName();
        questionTemplate.setTemplate(
                "if " + persist.getRandomName() + " has " + random.nextInt(0, 10)
                +" " + object + " and he " + persist.getRandomActionName() +" " + random.nextInt(0, 10)
                +" " + object + " how many does he have right now."
        ); //אם זה לא מובן לכם זה בגלל דניאל ברזסקי
        questionTemplate.setCreationDate(new java.util.Date());
        persist.save(questionTemplate);
        return new QuestionTemplateResponse(true, questionTemplate, null);
    }

    @RequestMapping("/submit-answer")
    //צריך לבדוק לגבי המשתנים
    //צריך פונקציה לחישוב את התשובה מהתבנית כדי לדעת אם היא נכונה
    public BasicResponse submitAnswer(String studentToken, int raceId, String answer, int questionId) {
        return new BasicResponse(true, null);
    }

}