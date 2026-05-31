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
//        if (race == null) {
//            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
//        }

        List<StudentDTO> response = new ArrayList<>();
        List<StudentEntity> studenList = persist.getAllStudentsByRaceID(raceId);
        for(StudentEntity student : studenList) {
            TrackEntity track = persist.getTrackByStudentToken((student.getToken()));
            StudentDTO studentDTO = new StudentDTO(student.getId(),student.getFullName(),track.getScore(),track.getPath(),track.getPowerUp());
            response.add(studentDTO);
        }

        return new RaceStudentsResponse(true, null, response, race.getGoalScore());
    }

    private final Map<String, Map<String,List<Point>>> listOfPoints = getMap();

    private Map<String, Map<String, List<Point>>> getMap() {
        Map<String, Map<String, List<Point>>> numMap = new HashMap<>();

        // פלוס
        numMap.put("+", createDifficultyMap(
                // Easy חד ספרתי
                List.of(
                        new Point(1,2), new Point(2,3), new Point(3,4), new Point(4,5), new Point(5,6), new Point(6,7), new Point(7,8), new Point(8,9), new Point(9,1), new Point(1,3),
                        new Point(2,4), new Point(3,5), new Point(4,6), new Point(5,7), new Point(6,8), new Point(7,9), new Point(8,1), new Point(9,2), new Point(1,4), new Point(2,5),
                        new Point(3,6), new Point(4,7), new Point(5,8), new Point(6,9), new Point(7,1), new Point(8,2), new Point(9,3), new Point(1,5), new Point(2,6), new Point(3,7),
                        new Point(4,8), new Point(5,9), new Point(6,1), new Point(7,2), new Point(8,3), new Point(9,4), new Point(1,6), new Point(2,7), new Point(3,8), new Point(4,9),
                        new Point(5,1), new Point(6,2), new Point(7,3), new Point(8,4), new Point(9,5), new Point(1,7), new Point(2,8), new Point(3,9), new Point(4,1), new Point(5,2)
                ),
                // Medium דו ספרתי/ משולב
                List.of(
                        new Point(15,5), new Point(23,14), new Point(35,21), new Point(42,15), new Point(56,22), new Point(64,31), new Point(71,18), new Point(85,13), new Point(44,25), new Point(33,16),
                        new Point(19,11), new Point(28,15), new Point(37,24), new Point(46,9), new Point(8,25), new Point(7,34), new Point(76,17), new Point(89,12), new Point(55,28), new Point(48,33),
                        new Point(21,7), new Point(8,26), new Point(45,35), new Point(52,41), new Point(63,27), new Point(74,8), new Point(81,29), new Point(92,15), new Point(66,5), new Point(77,6),
                        new Point(14,28), new Point(25,37), new Point(36,49), new Point(47,16), new Point(53,24), new Point(8,13), new Point(79,5), new Point(84,36), new Point(41,52), new Point(39,44),
                        new Point(9,42), new Point(29,10), new Point(31,65), new Point(43,26), new Point(57,2), new Point(61,8), new Point(73,19), new Point(86,23), new Point(95,4), new Point(54,39)
                ),
                // Hard תלת ספרתי/ משולב
                List.of(
                        new Point(125,45), new Point(234,115), new Point(345,210), new Point(456,325), new Point(567,142), new Point(678,51), new Point(789,134), new Point(890,215), new Point(145,35), new Point(256,412),
                        new Point(367,180), new Point(478,295), new Point(59,314), new Point(69,425), new Point(712,156), new Point(823,267), new Point(934,178), new Point(158,40), new Point(269,531), new Point(370,64),
                        new Point(481,50), new Point(592,61), new Point(603,472), new Point(714,583), new Point(825,194), new Point(936,205), new Point(17,516), new Point(258,67), new Point(369,738), new Point(47,849),
                        new Point(195,123), new Point(284,234), new Point(33,345), new Point(62,456), new Point(551,567), new Point(640,68), new Point(739,189), new Point(828,29), new Point(917,32), new Point(186,423),
                        new Point(275,54), new Point(34,645), new Point(453,76), new Point(542,67), new Point(631,178), new Point(720,89), new Point(819,390), new Point(908,415), new Point(135,52), new Point(246,637)
                )
        ));

        // מינוס
        numMap.put("-", createDifficultyMap(
                // Easy מספרים נמוכים
                List.of(
                        new Point(9,2), new Point(8,3), new Point(7,4), new Point(6,5), new Point(5,1), new Point(4,2), new Point(3,1), new Point(9,5), new Point(8,4), new Point(7,2),
                        new Point(10,3), new Point(12,5), new Point(15,6), new Point(14,7), new Point(11,4), new Point(13,8), new Point(16,9), new Point(18,9), new Point(17,8), new Point(15,9),
                        new Point(9,7), new Point(8,6), new Point(7,5), new Point(6,3), new Point(5,4), new Point(4,1), new Point(8,2), new Point(9,4), new Point(7,3), new Point(6,2),
                        new Point(11,6), new Point(12,8), new Point(13,4), new Point(14,5), new Point(15,7), new Point(16,8), new Point(17,9), new Point(10,5), new Point(12,3), new Point(11,7),
                        new Point(9,1), new Point(8,7), new Point(7,6), new Point(6,4), new Point(5,3), new Point(4,3), new Point(9,8), new Point(8,5), new Point(7,1), new Point(6,1)
                ),
                // Medium דו ספרתי/ משולב
                List.of(
                        new Point(45,12), new Point(68,23), new Point(57,34), new Point(89,45), new Point(76,51), new Point(94,62), new Point(38,5), new Point(59,6), new Point(47,10), new Point(85,42),
                        new Point(52,8), new Point(64,27), new Point(73,35), new Point(81,46), new Point(95,58), new Point(43,19), new Point(56,28), new Point(67,39), new Point(72,44), new Point(84,5),
                        new Point(60,5), new Point(70,35), new Point(80,45), new Point(90,55), new Point(50,15), new Point(40,25), new Point(85,5), new Point(75,5), new Point(65,5), new Point(95,45),
                        new Point(48,9), new Point(57,8), new Point(66,47), new Point(75,56), new Point(84,65), new Point(93,74), new Point(51,32), new Point(62,3), new Point(71,52), new Point(82,3),
                        new Point(98,14), new Point(87,25), new Point(76,36), new Point(65,47), new Point(54,18), new Point(43,26), new Point(92,7), new Point(81,8), new Point(70,19), new Point(61,8)
                ),
                // Hard: חיסור תלת-ספרתי/ משולב
                List.of(
                        new Point(345,120), new Point(456,23), new Point(567,342), new Point(678,453), new Point(789,564), new Point(890,675), new Point(234,113), new Point(543,221), new Point(654,332), new Point(765,43),
                        new Point(412,85), new Point(523,296), new Point(634,317), new Point(745,428), new Point(856,539), new Point(967,64), new Point(321,194), new Point(432,215), new Point(543,36), new Point(654,437),
                        new Point(500,45), new Point(600,36), new Point(700,46), new Point(800,78), new Point(900,69), new Point(400,152), new Point(505,263), new Point(606,374), new Point(707,485), new Point(808,596),
                        new Point(341,78), new Point(452,289), new Point(563,390), new Point(674,411), new Point(785,52), new Point(896,633), new Point(230,44), new Point(541,255), new Point(652,366), new Point(763,477),
                        new Point(489,299), new Point(590,88), new Point(611,47), new Point(722,566), new Point(833,655), new Point(944,744), new Point(355,66), new Point(466,277), new Point(577,38), new Point(688,499)
                )
        ));

        // כפל
        numMap.put("*", createDifficultyMap(
                // Easy חד ספרתי
                List.of(
                        new Point(2,3), new Point(4,5), new Point(6,7), new Point(8,9), new Point(3,4), new Point(5,6), new Point(7,8), new Point(9,2), new Point(2,4), new Point(3,5),
                        new Point(4,6), new Point(5,7), new Point(6,8), new Point(7,9), new Point(8,2), new Point(9,3), new Point(2,5), new Point(3,6), new Point(4,7), new Point(5,8),
                        new Point(6,9), new Point(7,2), new Point(8,3), new Point(9,4), new Point(2,6), new Point(3,7), new Point(4,8), new Point(5,9), new Point(6,2), new Point(7,3),
                        new Point(8,4), new Point(9,5), new Point(2,7), new Point(3,8), new Point(4,9), new Point(5,2), new Point(6,3), new Point(7,4), new Point(8,5), new Point(9,6),
                        new Point(2,8), new Point(3,9), new Point(4,2), new Point(5,3), new Point(6,4), new Point(7,5), new Point(8,6), new Point(9,7), new Point(2,9), new Point(3,2)
                ),
                // Medium דו ספרתי עם חד ספרתי
                List.of(
                        new Point(12,3), new Point(15,4), new Point(18,5), new Point(21,6), new Point(24,7), new Point(27,8), new Point(14,2), new Point(16,3), new Point(19,4), new Point(22,5),
                        new Point(25,6), new Point(28,7), new Point(11,8), new Point(13,9), new Point(17,2), new Point(20,3), new Point(23,4), new Point(26,5), new Point(29,6), new Point(15,7),
                        new Point(12,8), new Point(14,9), new Point(16,4), new Point(18,6), new Point(21,3), new Point(24,5), new Point(27,2), new Point(19,7), new Point(22,8), new Point(25,9),
                        new Point(28,4), new Point(11,5), new Point(13,6), new Point(17,7), new Point(20,8), new Point(23,9), new Point(26,2), new Point(29,3), new Point(15,5), new Point(12,6),
                        new Point(14,7), new Point(16,8), new Point(18,9), new Point(21,4), new Point(24,2), new Point(27,3), new Point(19,5), new Point(22,6), new Point(25,7), new Point(28,8)
                ),
                // Hard דו-ספרתי עם דו-ספרתי
                List.of(
                        new Point(15,12), new Point(23,14), new Point(35,21), new Point(42,15), new Point(56,22), new Point(64,31), new Point(71,18), new Point(85,13), new Point(44,25), new Point(33,16),
                        new Point(19,11), new Point(28,15), new Point(37,24), new Point(46,19), new Point(58,25), new Point(67,34), new Point(76,17), new Point(89,12), new Point(55,28), new Point(48,33),
                        new Point(21,19), new Point(34,26), new Point(45,35), new Point(52,41), new Point(63,27), new Point(74,38), new Point(81,29), new Point(92,15), new Point(66,45), new Point(77,36),
                        new Point(14,28), new Point(25,37), new Point(36,49), new Point(47,16), new Point(53,24), new Point(68,13), new Point(79,25), new Point(84,36), new Point(41,52), new Point(39,44),
                        new Point(17,42), new Point(29,51), new Point(31,65), new Point(43,26), new Point(57,32), new Point(61,48), new Point(73,19), new Point(86,23), new Point(95,14), new Point(54,39)
                )
        ));

        // חילוק
        // חילוק (/)
        numMap.put("/", createDifficultyMap(
                // Easy חילוק בסיסי
                List.of(
                        new Point(2,2), new Point(4,2), new Point(6,2), new Point(8,2), new Point(10,2), new Point(12,2), new Point(14,2), new Point(16,2), new Point(18,2), new Point(20,2),
                        new Point(3,3), new Point(6,3), new Point(9,3), new Point(12,3), new Point(15,3), new Point(18,3), new Point(21,3), new Point(24,3), new Point(27,3), new Point(30,3),
                        new Point(4,4), new Point(8,4), new Point(12,4), new Point(16,4), new Point(20,4), new Point(24,4), new Point(28,4), new Point(32,4), new Point(36,4), new Point(40,4),
                        new Point(5,5), new Point(10,5), new Point(15,5), new Point(20,5), new Point(25,5), new Point(30,5), new Point(35,5), new Point(40,5), new Point(45,5), new Point(50,5),
                        new Point(6,6), new Point(12,6), new Point(18,6), new Point(24,6), new Point(30,6), new Point(36,6), new Point(42,6), new Point(48,6), new Point(54,6), new Point(60,6)
                ),

                // Medium חילוק מספרים יותר גבוהים ותוצאות דו ספרתי
                List.of(
                        new Point(7,7), new Point(14,7), new Point(21,7), new Point(28,7), new Point(35,7), new Point(42,7), new Point(49,7), new Point(56,7), new Point(63,7), new Point(70,7),
                        new Point(8,8), new Point(16,8), new Point(24,8), new Point(32,8), new Point(40,8), new Point(48,8), new Point(56,8), new Point(64,8), new Point(72,8), new Point(80,8),
                        new Point(9,9), new Point(18,9), new Point(27,9), new Point(36,9), new Point(45,9), new Point(54,9), new Point(63,9), new Point(72,9), new Point(81,9), new Point(90,9),
                        new Point(22,2), new Point(24,2), new Point(26,2), new Point(28,2), new Point(30,2), new Point(33,3), new Point(36,3), new Point(39,3), new Point(42,3), new Point(45,3),
                        new Point(44,4), new Point(48,4), new Point(52,4), new Point(56,4), new Point(60,4), new Point(55,5), new Point(60,5), new Point(65,5), new Point(70,5), new Point(75,5)
                ),

                // Hard הוספת חלוקה בתלת ספרתי
                List.of(
                        new Point(66,6), new Point(72,6), new Point(78,6), new Point(84,6), new Point(90,6), new Point(77,7), new Point(84,7), new Point(91,7), new Point(98,7), new Point(105,7),
                        new Point(88,8), new Point(96,8), new Point(104,8), new Point(112,8), new Point(120,8), new Point(99,9), new Point(108,9), new Point(117,9), new Point(126,9), new Point(135,9),
                        new Point(110,10), new Point(120,10), new Point(130,10), new Point(140,10), new Point(150,10), new Point(121,11), new Point(132,11), new Point(143,11), new Point(154,11), new Point(165,11),
                        new Point(132,12), new Point(144,12), new Point(156,12), new Point(168,12), new Point(180,12), new Point(143,13), new Point(156,13), new Point(169,13), new Point(182,13), new Point(195,13),
                        new Point(154,14), new Point(168,14), new Point(182,14), new Point(196,14), new Point(210,14), new Point(165,15), new Point(180,15), new Point(195,15), new Point(210,15), new Point(225,15)
                )
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
            RaceEntity race = track.getRace();
            if (track.getScore() >= 10){ //CHANGE TO race.getGoalScore()
                race.setStatus(RACE_STATUS_FINISHED);
                persist.save(race);
                //clear up (the tracks???) and the questions
                sseManager.gameFinished(race.getId(), studentEntity.getId() ,studentEntity.getFullName());
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

        // 4. Construct the DTO collection
        List<StudentDTO> responseList = new ArrayList<>();
        List<StudentEntity> studentList = persist.getAllStudentsByRaceID(raceId);

        for (StudentEntity student : studentList) {
            TrackEntity track = persist.getTrackByRaceIDAndStudentID(raceId, student.getId());
            if (track != null) {
                StudentDTO dto = new StudentDTO(
                        student.getId(),
                        student.getFullName(),
                        track.getScore(),
                        track.getPath(),
                        track.getPowerUp()
                );
                responseList.add(dto);
            }
        }

        // 5. Sort descending by score right here on the server
        responseList.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Reusing your existing RaceStudentsResponse model cleanly!
        return new RaceStudentsResponse(true, null, responseList, race.getGoalScore());
    }



}