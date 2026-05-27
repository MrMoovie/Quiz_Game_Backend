package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import com.quiz_game.service.SseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.quiz_game.utils.Constants.*;
import static com.quiz_game.utils.Errors.*;

@RestController
public class GameController {
    @Autowired
    private Persist persist;
    @Autowired
    private SseManager sseManager;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/get-all-students-in-race") //MAJOR SECURITY WARNING
    public BasicResponse getAllStudents(String teacherToken, int raceId) {
        TeacherEntity teacherEntity = persist.getTeacherByToken(teacherToken);
        if (teacherEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED); //ERROR_WRONG_CREDENTIALS
        }
        if (!persist.isTeacherHostingRace(teacherEntity, raceId)) { // Can do only getRaces and check for null
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }
        List<StudentEntity> studenList = persist.getAllStudentsByRaceID(raceId);
        return new RaceStudentsResponse(true, null, studenList);
    }

    private final Map<String, Map<String,List<Point>>> listOfPoints = getMap();

    private Map<String, Map<String, List<Point>>> getMap() {
        Map<String, Map<String, List<Point>>> numMap = new HashMap<>();

        // פלוס
        numMap.put("+", createDifficultyMap(
                List.of(new Point(1,1)), // Easy
                List.of(new Point(2,2)), // Medium
                List.of(new Point(3,3))  // Hard
        ));

        // מינוס
        numMap.put("-", createDifficultyMap(
                List.of(new Point(1,1)), // Easy
                List.of(new Point(2,2)), // Medium
                List.of(new Point(3,3))  // Hard
        ));

        // חילוק
        numMap.put("/", createDifficultyMap(
                List.of(new Point(1,1)), // Easy
                List.of(new Point(2,2)), // Medium
                List.of(new Point(3,3))  // Hard
        ));

        // כפל
        numMap.put("*", createDifficultyMap(
                List.of(new Point(1,1)), // Easy
                List.of(new Point(2,2)), // Medium
                List.of(new Point(3,3))  // Hard
        ));

        return numMap;
    }

    // פונקציית עזר שמקבלת את רשימות הנקודות ומארגנת אותן בתוך ה-Map הפנימי
    private Map<String, List<Point>> createDifficultyMap(List<Point> easyList, List<Point> medList, List<Point> hardList) {
        Map<String, List<Point>> difficultyMap = new HashMap<>();

        // עטיפה ב-ArrayList כדי שהרשימות לא יהיו חסומות לשינויים (Mutable)
        difficultyMap.put("Easy", new ArrayList<>(easyList));
        difficultyMap.put("Medium", new ArrayList<>(medList));
        difficultyMap.put("Hard", new ArrayList<>(hardList));

        return difficultyMap;
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
        if (pathChoice < 0 || pathChoice > 2) { // pathChoice = 0 (normal) || 1 (dirt road) (easy) || 2 (highway) (hard)
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        String level = pathChoice == 0 ? "Medium" : pathChoice == 1 ? "Easy" : "Hard";

        QuestionTemplateEntity questionTemplate = persist.getRandomTemplate(level);

        ObjectEntity object = persist.getRandomObject();

        ActionEntity action = persist.getRandomAction();

        NameEntity name = persist.getRandomName();

        Random random = new Random();

     //   int max = questionTemplate.getMaxNumber();
        List<Point> points = listOfPoints.get(action.getActionOperation()).get(level);
        int index = random.nextInt(points.size());
        int num1 = points.get(index).x;
        int num2 = points.get(index).y;


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
       // System.out.println(rightAnswer);
        //calculate score
        if(rightAnswer) {
            question.setAnswerRight(true);
            persist.save(question);
            int addedScore = question.getScore();
            TrackEntity track = persist.getTrackByTrackId(trackId);
            track.setScore(track.getScore() + addedScore);
            track.setCurrentQuestionId(question.getId());
            persist.save(track);
            if (track.getScore() == 1000){
                RaceEntity race = track.getRace();
                race.setStatus(RACE_STATUS_FINISHED);
                persist.save(race);
            }
            sseManager.scoreEvent(track.getRace().getId(), studentEntity.getId(), track.getScore(), track.getPosition(), track.getCurrentQuestionId());
        }
        return new QuestionResponse(question);
    }


    @RequestMapping("/set-track")
    public BasicResponse setTrack(int trackId, int path, int pathChance, int powerUp) {
        TrackEntity track = persist.getTrackByTrackId(trackId);
        if (track == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }

        track.setPath(path);
        track.setPathChance(pathChance);
        track.setPowerUp(powerUp);
        persist.save(track);

        //[!]
       // sseManager.scoreEvent(track.getRace().getId(), track.getStudent().getId(), track.getScore(), track.getPosition());

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
        // track entity can't exist without race, so race can't be null.
        RaceEntity race = persist.getRaceByRaceId(track.getRace().getId());
        race.setStatus(status);
        persist.save(race);
        return new RaceResponse(race);
    }



}