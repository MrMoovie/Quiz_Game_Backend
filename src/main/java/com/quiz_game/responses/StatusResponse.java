package com.quiz_game.responses;

public class StatusResponse extends BasicResponse {
    private final Integer status;

    public StatusResponse(boolean success, Integer status, Integer errorCode) {
        super(success, errorCode);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
