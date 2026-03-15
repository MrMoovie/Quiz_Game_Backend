package com.quiz_game.responses;

public class JoinSessionResponse {
    private boolean success;
    private Integer errorCode;
    private Integer sessionId;

    public JoinSessionResponse() {
    }

    public JoinSessionResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public JoinSessionResponse(boolean success, Integer errorCode, Integer sessionId) {
        this.success = success;
        this.errorCode = errorCode;
        this.sessionId = sessionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }
}

