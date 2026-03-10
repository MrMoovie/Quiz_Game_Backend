package com.ashcollege.responses;

import com.ashcollege.entities.BasicUser;

import static com.ashcollege.utils.Constants.USER_TYPE_CLIENT;
import static com.ashcollege.utils.Constants.USER_TYPE_PROFESSIONAL;

public class DefaultParamResponse extends BasicResponse{
    private int userType;

    public DefaultParamResponse (boolean success, Integer errorCode,
                                 BasicUser basicUser) {
        super(success, errorCode);
        if (basicUser instanceof com.ashcollege.entities.ClientEntity) {
            this.userType = USER_TYPE_CLIENT;
        } else if (basicUser instanceof com.ashcollege.entities.ProffesionalEntity) {
            this.userType = USER_TYPE_PROFESSIONAL;
        }
    }

    public int getUserType() {
        return userType;
    }
}
