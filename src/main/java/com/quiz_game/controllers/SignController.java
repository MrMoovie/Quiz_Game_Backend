package com.quiz_game.controllers;

import com.quiz_game.entities.BasicUser;
import com.quiz_game.responses.BasicResponse;
import com.quiz_game.responses.LoginResponse; // ייבוא ה-Response המפורט
import com.quiz_game.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.quiz_game.utils.Errors.ERROR_WRONG_CREDENTIALS;

@RestController
public class SignController {

    @Autowired
    private Persist persist;

    /**
     * הרשמה למערכת - Sign Up
     * מקבל: שם משתמש, סיסמה, שם מלא וסוג משתמש
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public BasicResponse signUp(String username, String password, String fullName, int userType) {
        BasicResponse response;

        //  בדיקה אם שם המשתמש כבר תפוס במערכת
        BasicUser existingUser = persist.getUserByUsername(username);

        if (existingUser == null) {
            //  יצירת אובייקט משתמש חדש
            BasicUser newUser = new BasicUser();
            newUser.setUsername(username);
            newUser.setPassword(password); // הערה: בפרויקט אמיתי מומלץ להצפין כאן
            newUser.setFullName(fullName);
            newUser.setUserType(userType);

            //  יצירת Token ייחודי עבור המשתמש (מזהה ה-Session שלו)
            String token = UUID.randomUUID().toString();
            newUser.setToken(token);

            //  שמירה למסד הנתונים בעזרת ה-Persist
            persist.save(newUser);

            //  החזרת תשובה חיובית
            response = new BasicResponse(true, null);
        } else {
            // שם המשתמש קיים - מחזירים שגיאה (קוד 1 למשל עבור User Exists)
            response = new BasicResponse(false, 1);
        }

        return response;
    }

    /**
     * התחברות למערכת - Sign In
     * מקבל: שם משתמש וסיסמה
     */
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public LoginResponse signIn(String username, String password) {
        //  חיפוש המשתמש לפי שם משתמש וסיסמה
        BasicUser user = (BasicUser) persist.getUserByUsernameAndPassword(username, password);

        if (user != null) {
            //  אם המשתמש נמצא, נעדכן/ניצור טוקן חדש לחיבור הנוכחי
            String newToken = UUID.randomUUID().toString();
            user.setToken(newToken);
            persist.save(user); // עדכון הטוקן ב-DB

            //  החזרת LoginResponse מפורט עם כל הנתונים שה-Frontend צריך
            // פרמטרים לפי הקונסטרקטור שלך: success, errorCode, permission, token, id, userType
            return new LoginResponse(
                    true,           // success
                    null,           // errorCode
                    1,              // permission (ניתן להתאים לפי הצורך)
                    newToken,       // token
                    user.getId(),   // id (נשלף מה-Entity)
                    user.getUserType()      // userType (למשל 1 לסטודנט, 2 למורה)
            );
        } else {
            // פרטים לא נכונים - מחזירים שגיאה תואמת
            return new LoginResponse(false, ERROR_WRONG_CREDENTIALS, 0, null, 0, 0);
        }
    }
}