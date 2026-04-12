package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.BasicResponse;
import com.quiz_game.responses.LoginResponse; // ייבוא ה-Response המפורט
import com.quiz_game.service.Persist;
import com.quiz_game.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.quiz_game.utils.Constants.*;
import static com.quiz_game.utils.Errors.*;

@RestController
public class SignController {

    @Autowired
    private Persist persist;

    /**
     * הרשמה למערכת - Sign Up
     * מקבל: שם משתמש, סיסמה, שם מלא וסוג משתמש
     */
    @RequestMapping("/signup")
    public BasicResponse addUser(int selectedType, String username, String password, String fullName) {
        try {
            if (username != null && password != null && fullName != null && selectedType != 0) {
                BasicUser userEntity = persist.getUserByUsername(username);
                if (userEntity != null) {
                    return new BasicResponse(false, ERROR_USERNAME_ALREADY_EXISTS);
                } else {
                    if (selectedType == USER_TYPE_STUDENT) {
                        StudentEntity studentEntity = new StudentEntity();
                        studentEntity.setUsername(username);
                        studentEntity.setPassword(password);
                        studentEntity.setFullName(fullName);
                        String token = GeneralUtils.hashMd5(username, password);
                        studentEntity.setToken(token);
                        persist.save(studentEntity);
                        return new LoginResponse(true, null, 1, token, studentEntity.getId(), selectedType);
                    } else {
                        TeacherEntity teacherEntity = new TeacherEntity();
                        teacherEntity.setFullName(fullName);
                        teacherEntity.setUsername(username);
                        teacherEntity.setPassword(password);
                        String token = GeneralUtils.hashMd5(username, password);
                        teacherEntity.setToken(token);
                        persist.save(teacherEntity);
                        return new LoginResponse(true, null, 1, token, teacherEntity.getId(), selectedType);
                    }
                }
            } else {
                return new BasicResponse(false, ERROR_MISSING_VALUES);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping ("/login")
    public BasicResponse getUser (String username, String password, int selectedType) {
        try {
            if (username != null && password != null) {
                if (selectedType == USER_TYPE_STUDENT) {
                    StudentEntity studentEntity = persist.getStudentByUsernameAndPassword(username, password);
                    if (studentEntity != null) {
                        String token = GeneralUtils.hashMd5(username, password);
                        studentEntity.setToken(token);
                        persist.save(studentEntity);
                        return new LoginResponse(true, null, 1, token, studentEntity.getId(), selectedType);
                    } else {
                        return new BasicResponse(false,  ERROR_WRONG_CREDENTIALS);
                    }
                } else {
                    TeacherEntity teacherEntity = persist.getTeacherByUsernameAndPassword(username, password);
                    if (teacherEntity != null) {
                        String token = GeneralUtils.hashMd5(username, password);
                        teacherEntity.setToken(token);
                        persist.save(teacherEntity);
                        return new LoginResponse(true, null, 1, token, teacherEntity.getId(), selectedType);
                    } else {
                        return new BasicResponse(false,  ERROR_WRONG_CREDENTIALS);
                    }

                }
            } else {
                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    // >> wrong persist function, not following the conventions of the code << //
//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public BasicResponse signUp(String username, String password, String fullName, int userType) {
//        BasicResponse response;
//
//        //  בדיקה אם שם המשתמש כבר תפוס במערכת
//        BasicUser existingUser = persist.getUserByUsername(username);
//
//        if (existingUser == null) {
//            //  יצירת אובייקט משתמש חדש
//            BasicUser newUser = new BasicUser();
//            newUser.setUsername(username);
//            newUser.setPassword(password); // הערה: בפרויקט אמיתי מומלץ להצפין כאן
//            newUser.setFullName(fullName);
//            newUser.setUserType(userType);
//
//            //  יצירת Token ייחודי עבור המשתמש (מזהה ה-Session שלו)
//            String token = UUID.randomUUID().toString();
//            newUser.setToken(token);
//
//            //  שמירה למסד הנתונים בעזרת ה-Persist
//            persist.save(newUser);
//
//            //  החזרת תשובה חיובית
//            response = new BasicResponse(true, null);
//        } else {
//            // שם המשתמש קיים - מחזירים שגיאה (קוד 1 למשל עבור User Exists)
//            response = new BasicResponse(false, 1);
//        }
//
//        return response;
//    }

    /**
     * התחברות למערכת - Sign In
     * מקבל: שם משתמש וסיסמה
     */

    // >> wrong persist function, not following the conventions of the code << //
//    @RequestMapping(value = "/signin", method = RequestMethod.POST)
//    public LoginResponse signIn(String username, String password) {
//        //  חיפוש המשתמש לפי שם משתמש וסיסמה
//        BasicUser user = (BasicUser) persist.getUserByUsernameAndPassword(username, password);
//
//        if (user != null) {
//            //  אם המשתמש נמצא, נעדכן/ניצור טוקן חדש לחיבור הנוכחי
//            String newToken = UUID.randomUUID().toString();
//            user.setToken(newToken);
//            persist.save(user); // עדכון הטוקן ב-DB
//
//            //  החזרת LoginResponse מפורט עם כל הנתונים שה-Frontend צריך
//            // פרמטרים לפי הקונסטרקטור שלך: success, errorCode, permission, token, id, userType
//            return new LoginResponse(
//                    true,           // success
//                    null,           // errorCode
//                    1,              // permission (ניתן להתאים לפי הצורך)
//                    newToken,       // token
//                    user.getId(),   // id (נשלף מה-Entity)
//                    user.getUserType()      // userType (למשל 1 לסטודנט, 2 למורה)
//            );
//        } else {
//            // פרטים לא נכונים - מחזירים שגיאה תואמת
//            return new LoginResponse(false, ERROR_WRONG_CREDENTIALS, 0, null, 0, 0);
//        }
//    }
}