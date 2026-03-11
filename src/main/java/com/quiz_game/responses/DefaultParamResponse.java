package com.quiz_game.responses;

import com.quiz_game.entities.BasicUser;

import static com.quiz_game.utils.Constants.USER_TYPE_CLIENT;
import static com.quiz_game.utils.Constants.USER_TYPE_PROFESSIONAL;

public class DefaultParamResponse extends BasicResponse{
    private int userType;

    public DefaultParamResponse (boolean success, Integer errorCode,
                                 BasicUser basicUser) {
        super(success, errorCode);
        if (basicUser instanceof com.quiz_game.entities.ClientEntity) {
            this.userType = USER_TYPE_CLIENT;
        } else if (basicUser instanceof com.quiz_game.entities.ProffesionalEntity) {
            this.userType = USER_TYPE_PROFESSIONAL;
        }
    }

    public int getUserType() {
        return userType;
    }
}
