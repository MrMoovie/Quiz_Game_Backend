package com.quiz_game.responses;

public class JoinRaceResponse extends BasicResponse {
    private Integer raceId;

    public JoinRaceResponse() {
    }


    public JoinRaceResponse(boolean success, Integer errorCode, Integer raceId) {
        super(success, errorCode);
        this.raceId = raceId;
    }


    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }
}

