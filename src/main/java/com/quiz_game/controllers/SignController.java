package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.BasicResponse;
import com.quiz_game.responses.LoginResponse; // ייבוא ה-Response המפורט
import com.quiz_game.service.Persist;
import com.quiz_game.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/signup")
    public BasicResponse addUser(int selectedType, String username, String password, String confirmPassword, String fullName) {
        if (isNotValidating(fullName) || !fullName.trim().contains(" ")) {
            return new BasicResponse(false, ERROR_FULL_NAME_REQUIRED);

        }
        if (isNotValidating(username)) {
            return new BasicResponse(false, ERROR_USERNAME_REQUIRED);
        }

        if (isNotValidating(password) || password.trim().length() < 6) {
            return new BasicResponse(false, ERROR_PASSWORD_REQUIRED);
        }
        if (!password.equals(confirmPassword)) {
            return new BasicResponse(false, ERROR_PASSWORDS_MISMATCH);
        }

        BasicUser userEntity = persist.getUserByUsername(username);
        if (userEntity != null) {
            return new BasicResponse(false, ERROR_USERNAME_ALREADY_EXISTS);
        }

        if (selectedType == USER_TYPE_STUDENT) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setUsername(username);
            studentEntity.setPassword(password);
            studentEntity.setFullName(fullName);
            String token = GeneralUtils.hashMd5(username, password);
            studentEntity.setToken(token);
            persist.save(studentEntity);
            return new LoginResponse(true, null, token, studentEntity.getId(), selectedType);
        } else if (selectedType == USER_TYPE_TEACHER) {
            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setFullName(fullName);
            teacherEntity.setUsername(username);
            teacherEntity.setPassword(password);
            String token = GeneralUtils.hashMd5(username, password);
            teacherEntity.setToken(token);
            persist.save(teacherEntity);
            return new LoginResponse(true, null, token, teacherEntity.getId(), selectedType);
        }
        return new BasicResponse(false, ERROR_MISSING_VALUES);
    }

    private boolean isNotValidating(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        boolean hasLetter = str.matches(".*[a-zA-Z].*");
        boolean hasDigit = str.matches(".*[0-9].*");

        return !hasLetter && !hasDigit;
    }

    @RequestMapping("/login")
    public BasicResponse getUser(String username, String password, int selectedType) {
        BasicUser userEntity = persist.getUserByUsername(username);
        if (selectedType != USER_TYPE_STUDENT && selectedType != USER_TYPE_TEACHER) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        if (userEntity == null || !userEntity.getPassword().equals(password)) {
            return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
        }
        if (userEntity instanceof StudentEntity && selectedType == USER_TYPE_TEACHER ||
                userEntity instanceof TeacherEntity && selectedType == USER_TYPE_STUDENT) {
            return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
        }
        return new LoginResponse(true, null, userEntity.getToken(), userEntity.getId(), selectedType);
    }
}