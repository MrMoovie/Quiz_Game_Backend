package com.quiz_game.responses;

import com.quiz_game.entities.BasicUser;

public class DefaultParamsResponse extends BasicResponse{
    private int userId;
    private String fullName;
    private int userType;
    public DefaultParamsResponse(boolean success, Integer errorCode, BasicUser basicUser){
        super(success, errorCode);
        this.userId = basicUser.getId();
        this.fullName = basicUser.getFullName();
        this.userType = basicUser.getUserType();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
