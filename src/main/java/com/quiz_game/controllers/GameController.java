package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
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

        QuestionTemplateEntity questionTemplate = new QuestionTemplateEntity();
        questionTemplate.setDeleted(false);
        Random random = new Random();
        questionTemplate.setDifficultyLevel(String.valueOf(pathChoice));

        String character = persist.getRandomName();
        String object = persist.getRandomObjectName();
        String action = persist.getRandomActionName();
        int has = random.nextInt(0, 10);
        int change = random.nextInt(1, 10); // origin must be 1.
        questionTemplate.setTemplate(
                "if " + character +
                        " has " + has + " " +
                        object + " and he " +
                        action + " " +
                        change + " " +
                        object + " how many does he have right now?"
        );
        questionTemplate.setCreationDate(new java.util.Date());
        persist.save(questionTemplate);
        return new QuestionTemplateResponse(true, questionTemplate, null);
    }


///  יש בעיה שמוגדר בדאטה בייס שבטבלת תבנית השאלה יכול להיות נוסח שאלה ייחודי בלבד
///  מה שיכול להיות בעיה אם נוצר רנדומלי 2 שאלות זהות
/// אולי להוריד את הייחודיות
/// **צריך בדיקה נוספת**
    @RequestMapping("/submit-answer")
    public BasicResponse submitAnswer(String studentToken, int trackId, int questionId, String answer, int pathChoice) {
        StudentEntity studentEntity = persist.getStudentByToken(studentToken);
        QuestionTemplateEntity questionTemplate = persist.getQuestionTemplateByQuestionId(questionId);

        if (studentEntity == null || questionTemplate == null ) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }

        if (!persist.isStudentInRace(studentEntity, trackId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_STUDENT);
        }

        if (answer == null) return new BasicResponse(false, ERROR_MISSING_VALUES);

        //  ניקוי רווחים וחילוץ המספר הראשון מהתשובה
        String studentNumberStr;
        java.util.regex.Matcher answerMatcher = java.util.regex.Pattern.compile("\\d+").matcher(answer);
        if (answerMatcher.find()) {
            // מצאתי את המספר בתשובה ושמתי אותו במשתנה:
            studentNumberStr = answerMatcher.group();
        } else {
            //לא נמצא מספר בתשובה שלך
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }




        QuestionTemplateResponse response = (QuestionTemplateResponse) getNewQuestion(studentToken, trackId, pathChoice);
        QuestionTemplateEntity question = new QuestionTemplateEntity();
        questionTemplate.setDeleted(response.isDeleted());
        questionTemplate.setDifficultyLevel(response.getDifficultyLevel());
        questionTemplate.setTemplate(response.getQuestionTemplate());
        questionTemplate.setCreationDate(response.getDate());

        //  השוואה
        String template = questionTemplate.getTemplate();
        double studentAnswerNum = Double.parseDouble(studentNumberStr);
        //בדיקת שוויון בין מספרים עשרוניים בעזרת "מרחק" קטן (Epsilon)
        // כדי למנוע טעויות דיוק של המחשב בחישובים (כמו חילוק)
        if (Math.abs(getResult(template)- studentAnswerNum) < 0.001) {
            persist.save(questionTemplate);
            return new RightAnswerResponse(true,question);
        } else {
            persist.save(questionTemplate);
            return new RightAnswerResponse(false,question);
        }
    }
    ///  צריך להוסיף עמודה בטבלת הactions שנקראת action operation ששם ליד כל שם של פעולה יהיה כתוב מה המשמעות המתמטית שלה
    private double getResult(String template) {
        //  חילוץ נתונים מה-Template (שני המספרים הראשונים שאני מוצא)
        java.util.regex.Matcher templateMatcher = java.util.regex.Pattern.compile("\\d+").matcher(template);

        int num1 = 0, num2 = 1;
        if (templateMatcher.find()) num1 = Integer.parseInt(templateMatcher.group());
        //  גfind מחפש את המספר הבא.
        if (templateMatcher.find()) num2 = Integer.parseInt(templateMatcher.group());

        // operation string from template
        if (template.contains("+") || template.contains("added") || template.contains("gives")) {
            return num1 + num2;
        } else if (template.contains("-") || template.contains("took") || template.contains("lost")) {
            return num1 - num2;
        } else if (template.contains("x") || template.contains("*") || template.contains("times")) {
            return num1 * num2;
        } else if (template.contains("/") || template.contains("divided")) {
            return (num2 != 0) ? (double) num1 / num2 : 0;
        }
        return 0;
    }

}