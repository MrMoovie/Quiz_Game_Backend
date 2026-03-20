package com.quiz_game.responses;

public class CreateRaceResponse extends BasicResponse {

    private Integer raceId;
    private String entryCode;

    public CreateRaceResponse() {
    }

    public CreateRaceResponse(boolean success, Integer errorCode, Integer raceId, String entryCode) {
        super(success, errorCode);
        this.raceId = raceId;
        this.entryCode = entryCode;
    }


    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
}
