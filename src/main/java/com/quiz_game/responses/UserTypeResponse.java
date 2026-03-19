package com.quiz_game.responses;

import com.quiz_game.entities.BasicUser;

import static com.quiz_game.utils.Constants.*;

public class UserTypeResponse extends BasicResponse{
    private int userType;

    public UserTypeResponse(boolean success, Integer errorCode, BasicUser basicUser) {
        super(success, errorCode);
        if (basicUser instanceof com.quiz_game.entities.StudentEntity) {
            this.userType = USER_TYPE_STUDENT;
        } else if (basicUser instanceof com.quiz_game.entities.TeacherEntity) {
            this.userType = USER_TYPE_TEACHER;
        }
    }

    public int getUserType() {
        return userType;
    }
}
