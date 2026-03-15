package com.quiz_game.responses;

public class CreateSessionResponse {
    private boolean success;
    private Integer errorCode;
    private Integer sessionId;
    private String entryCode;

    public CreateSessionResponse() {
    }

    public CreateSessionResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public CreateSessionResponse(boolean success, Integer errorCode, Integer sessionId, String entryCode) {
        this.success = success;
        this.errorCode = errorCode;
        this.sessionId = sessionId;
        this.entryCode = entryCode;
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

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
}
