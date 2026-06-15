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

    @RequestMapping("/get-all-students-in-race")
    public BasicResponse getAllStudents(String teacherToken, int raceId) {
        TeacherEntity teacherEntity = persist.getTeacherByToken(teacherToken);
        if (teacherEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED); //ERROR_WRONG_CREDENTIALS
        }
        if (!persist.isTeacherHostingRace(teacherEntity, raceId)) { // Can do only getRaces and check for null
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }
        RaceEntity race = persist.getRaceByRaceId(raceId);
        if (race == null) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }

        List<StudentEntity> studenList = persist.getAllStudentsByRaceID(raceId);
        if (studenList == null) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }
        for(StudentEntity student : studenList) {
            student.setToken("-1");
        }

        return new RaceStudentsResponse(true, null, studenList, null, race.getGoalScore());
    }

    private Point generateDynamicNumbers(String operation, String level) {
        Random random = new Random();
        int num1 = 0, num2 = 0;

        switch (operation) {
            case "+":
                if ("Easy".equals(level)) {
                    num1 = random.nextInt(9) + 1; // 1 to 9
                    num2 = random.nextInt(9) + 1;
                } else if ("Medium".equals(level)) {
                    num1 = random.nextInt(90) + 10; // 10 to 99
                    num2 = random.nextInt(90) + 10;
                } else { // Hard
                    num1 = random.nextInt(900) + 100; // 100 to 999
                    num2 = random.nextInt(900) + 100;
                }
                break;

            case "-":
                if ("Easy".equals(level)) {
                    num1 = random.nextInt(9) + 2; // 2 to 10
                    num2 = random.nextInt(num1 - 1) + 1; // Assures positive answer
                } else if ("Medium".equals(level)) {
                    num1 = random.nextInt(90) + 10; // 10 to 99
                    num2 = random.nextInt(num1 - 1) + 1;
                } else { // Hard
                    num1 = random.nextInt(900) + 100; // 100 to 999
                    num2 = random.nextInt(num1 - 1) + 1;
                }
                break;

            case "*":
                if ("Easy".equals(level)) {
                    // Focus: Core times tables up to 10x10
                    num1 = random.nextInt(9) + 2; // 2 to 10
                    num2 = random.nextInt(9) + 2; // 2 to 10
                } else if ("Medium".equals(level)) {
                    // Focus: Small 2-digit numbers by a single digit (e.g., 14 x 4 or 25 x 3)
                    num1 = random.nextInt(15) + 11; // 11 to 25
                    num2 = random.nextInt(5) + 2;   // 2 to 6
                } else { // Hard
                    // Focus: Harder 2-digit by 1-digit, forcing mental grouping (e.g., 84 x 7)
                    num1 = random.nextInt(75) + 15; // 15 to 89
                    num2 = random.nextInt(7) + 3;   // 3 to 9
                }
                break;

            case "/":
                int divisor, answer;
                if ("Easy".equals(level)) {
                    // Focus: Clean reversals of basic times tables (e.g., 42 / 6 = 7)
                    divisor = random.nextInt(9) + 2; // 2 to 10
                    answer = random.nextInt(9) + 2;  // 2 to 10
                } else if ("Medium".equals(level)) {
                    // Focus: Divisor is a single digit, answer is a clean teen (e.g., 72 / 4 = 18)
                    divisor = random.nextInt(7) + 2;  // 2 to 8
                    answer = random.nextInt(10) + 11; // 11 to 20
                } else { // Hard
                    // Focus: Standard middle school limits (e.g., 144 / 12 = 12 or 225 / 9 = 25)
                    divisor = random.nextInt(11) + 2; // 2 to 12
                    answer = random.nextInt(25) + 11; // 11 to 35
                }
                num1 = divisor * answer;
                num2 = divisor;
                break;
        }

        return new Point(num1, num2);
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

//        List<Point> points = listOfPoints.get(action.getActionOperation()).get(level);
//        int index = random.nextInt(points.size());
//        int num1 = points.get(index).x;
//        int num2 = points.get(index).y;
        Point generatedNumbers = generateDynamicNumbers(action.getActionOperation(), level);
        int num1 = generatedNumbers.x;
        int num2 = generatedNumbers.y;


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
        //calculate score
        if(rightAnswer) {
            question.setAnswerRight(true);
            persist.save(question);

            int addedScore = question.getScore();
            TrackEntity track = persist.getTrackByTrackId(trackId);
            track.setScore(track.getScore() + addedScore);
            track.setCurrentQuestionId(question.getId());
            persist.save(track);

            RaceEntity race = track.getRace();
            sseManager.scoreEvent(track.getRace().getId(), studentEntity.getId(), track.getScore(), track.getPosition(), track.getCurrentQuestionId());
            if (track.getScore() >= race.getGoalScore()){
                race.setStatus(RACE_STATUS_FINISHED);
                persist.save(race);
                sseManager.gameFinished(race.getId(), studentEntity.getId() ,studentEntity.getFullName());

                //bfr deleting we can add a function to save the history compactly(in a vector or something).
                persist.deleteAllQuestionsByRaceId(race.getId());
            }
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

    @RequestMapping("/get-race-results")
    public BasicResponse getRaceResults(String token, int raceId) {
        // 1. Authenticate the user (could be a student OR a teacher)
        BasicUser user = persist.getUserByToken(token);
        if (user == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }

        // 2. Fetch the target race
        RaceEntity race = persist.getRaceByRaceId(raceId);
        if (race == null) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        // 3. Security validation based on user role type
        if (user instanceof TeacherEntity) {
            if (!persist.isTeacherHostingRace((TeacherEntity) user, raceId)) {
                return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
            }
        } else if (user instanceof StudentEntity) {
            if (!persist.isStudentInSpecificRace((StudentEntity) user, raceId)) {
                return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
            }
        }

        List<TrackEntity> trackList = race.getTracks();
        List<TrackEntity> sortedTracks = trackList.stream()
                .peek(track -> {
                    if (track.getStudent() != null) {
                        track.getStudent().setToken("-1"); // הגנה: ניקוי הטוקן של הסטודנט בתוך הטרק
                    }
                })
                .sorted((trackA, trackB) -> Integer.compare(trackB.getScore(), trackA.getScore())) // מיון לפי score בטרק
                .toList();

        List<StudentEntity> sortedStudents = sortedTracks.stream()
                .map(TrackEntity::getStudent) // שולף את הסטודנט מתוך הטרק
                .filter(Objects::nonNull)     // הגנה למקרה שיש טרק בלי סטודנט
                .toList();

        // Reusing your existing RaceStudentsResponse model cleanly!
        return new RaceStudentsResponse(true, null, sortedStudents,sortedTracks, race.getGoalScore());
    }



}