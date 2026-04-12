package com.quiz_game.responses;

public class LoginResponse extends BasicResponse {
    private String token;
    private int id;
    private int userType;

    public LoginResponse(boolean success, Integer errorCode, String token, int id,
                         int userType) {
        super(success, errorCode);
        this.token = token;
        this.id = id;
        this.userType = userType;
    }

    public LoginResponse(int permission, String token, int id) {
        this.token = token;
        this.id = id;
    }

    public LoginResponse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
