package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.awt.*;
import java.util.Date;
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
    public BasicResponse getNewQuestion(String studentToken, int trackId, int pathChoice) {
        StudentEntity studentEntity = persist.getStudentByToken(studentToken);
        if (studentEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isStudentInRace(studentEntity, trackId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
        }
        if (pathChoice < 0 || pathChoice > 2) { // pathChoice = 0 (normal)  || 1 (dirt road) || 2 (highway)
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        String level = pathChoice == 0 ? "Medium" : pathChoice == 1 ? "Easy" : "Hard";

        QuestionTemplateEntity questionTemplate = persist.getRandomTemplate(level);

        ObjectEntity object = persist.getRandomObject();

        ActionEntity action = persist.getRandomAction();

        NameEntity name = persist.getRandomName();

        Random random = new Random();

        int max = questionTemplate.getMaxNumber();
        // צריך לעבוד על זה:
        int num1 = random.nextInt(2, max);
        int num2 = random.nextInt(2, max);


        String newQuestionTemplate = questionTemplate.getTemplate()
                .replace("{name}", name.getName())
                .replace("{object}", object.getObjectName())
                .replace("{action}", action.getActionName())
                .replace("{NUM1}", String.valueOf(num1))
                .replace("{NUM2}", String.valueOf(num2));


        int answer = switch (action.getActionOperation()) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            default -> 0;
        };

        QuestionEntity newQuestion = new QuestionEntity();

        //   if (!persist.isStudentInRace(studentEntity, trackId)) {
        //        return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
        //   }
        // אם הגענו לפה **חייב להיות track** ולכן הוא לא null ואין צורך לבדוק אם הוא null
        TrackEntity track = persist.getTrackByTrackId(trackId);

        newQuestion.setTrack(track);
        newQuestion.setQuestion(newQuestionTemplate);
        newQuestion.setAnswer(answer);
        newQuestion.setCreationDate(new Date());

        persist.save(newQuestion);

        return new QuestionResponse(newQuestion);
    }

    // אם צריך:}
    @RequestMapping("/getQuestion")
    public BasicResponse getExistingQuestion(int questionId) {
        QuestionEntity questionEntity = persist.getQuestionById(questionId);
        if (questionEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        return new QuestionResponse(questionEntity);
    }
    //}

    // באופן מוחלט אסור לגרום לכך שפונקציית getNewQuestion
    // תקרא ל submitAnswer כי אז יהיה לופ אינסופי של פונקציות רקורסיביות
    @RequestMapping("/submit-answer")
    public BasicResponse submitAnswer(String studentToken, int trackId, int questionId, int answer, int pathChoice) {
        StudentEntity studentEntity = persist.getStudentByToken(studentToken);
        QuestionEntity question = persist.getQuestionById(questionId);
        if (studentEntity == null || question == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isStudentInRace(studentEntity, trackId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
        }
        if (question.getTrack().getId() != trackId) {
            return new BasicResponse(false, ERROR_UNKNOWN_QUESTION_FOR_TRACK);
        }
        if (pathChoice < 0 || pathChoice > 2) { // pathChoice = 0 (normal)  || 1 (dirt road) || 2 (highway)
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        boolean rightAnswer = question.getAnswer() == answer;
        // לפי דעתי זה סלט אבל בסדר:
        // כאן אני גם כבר קורא לשאלה חדשה:
        BasicResponse basicResponse = getNewQuestion(studentToken, trackId, pathChoice);
        if (basicResponse.isSuccess()) {
            QuestionResponse questionResponse = (QuestionResponse) basicResponse;
            QuestionEntity newQuestion = questionResponse.getQuestion();
            return new RightAnswerResponse(rightAnswer, newQuestion);
        }
        return new RightAnswerResponse(rightAnswer, null);
    }

}