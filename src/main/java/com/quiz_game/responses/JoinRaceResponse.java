package com.quiz_game.responses;

public class
JoinRaceResponse extends BasicResponse {
    private Integer raceId;
    private Integer goalScore;

    public JoinRaceResponse() {
    }


    public JoinRaceResponse(boolean success, Integer errorCode, Integer raceId, Integer goalScore) {
        super(success, errorCode);
        this.raceId = raceId;
        this.goalScore = goalScore;
    }

    public Integer getGoalScore() {
        return goalScore;
    }

    public void setGoalScore(Integer goalScore) {
        this.goalScore = goalScore;
    }

    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }
}

