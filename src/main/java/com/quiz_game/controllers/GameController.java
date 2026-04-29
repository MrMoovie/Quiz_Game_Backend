package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.quiz_game.utils.Constants.*;
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
        if (pathChoice < 0 || pathChoice > 2) { // pathChoice = 0 (normal)  || 1 (dirt road) (easy) || 2 (highway) (hard)
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
        int score = pathChoice == 0 ? MEDIUM_Q_SCORE : pathChoice == 1 ? EASY_Q_SCORE : HARD_Q_SCORE;
        newQuestion.setScore(score);
        persist.save(newQuestion);

        return new QuestionResponse(newQuestion);
    }

    @RequestMapping("/getQuestion")
    public BasicResponse getExistingQuestion(int questionId) {
        QuestionEntity questionEntity = persist.getQuestionById(questionId);
        if (questionEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        return new QuestionResponse(questionEntity);
    }

    @RequestMapping("/submit-answer")
    public BasicResponse submitAnswer(String studentToken, int trackId, int questionId, int answer) {
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

        boolean rightAnswer = question.getAnswer() == answer;
        //calculate score
        int addedScore = question.getScore();
        TrackEntity track = persist.getTrackByTrackId(trackId);
        track.setScore(track.getScore()+addedScore);
        persist.save(track);
        return new RightAnswerResponse(rightAnswer, question);
    }


    @RequestMapping("/set-track")
    public BasicResponse setTrack(int trackId, int path, int pathChance, int powerUp, int position) {
        TrackEntity track = persist.getTrackByTrackId(trackId);
        if (track == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }

        track.setPath(path);
        track.setPathChance(pathChance);
        track.setPowerUp(powerUp);
        track.setPosition(position);
        persist.save(track);
        return new TrackResponse(track);
    }

    @RequestMapping("/get-track")
    public BasicResponse getTrack(String studentToken) {
        TrackEntity track = persist.getTrackByStudentToken(studentToken);
        if (track == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        return new TrackResponse(track);
    }

    @RequestMapping("/set-status-for-student")
    public BasicResponse setRaceStatusForStudent(int trackId, int status) {
        TrackEntity track = persist.getTrackByTrackId(trackId);
        if (track == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        // track entity cant exist without race, so race cant be null.
        RaceEntity race = persist.getRaceByRaceId(track.getRace().getId());
        race.setStatus(status);
        persist.save(race);
        return new RaceResponse(race);
    }



}