package com.quiz_game.responses;

import com.quiz_game.entities.RaceEntity;

import java.util.List;

public class RacesResponse extends BasicResponse {
    private List<RaceEntity> races;

    public RacesResponse() {
    }

    public RacesResponse(boolean success, Integer errorCode, List<RaceEntity> races) {
        super(success, errorCode);
        this.races = races;
    }

    public List<RaceEntity> getRaces() {
        return races;
    }

    public void setRaces(List<RaceEntity> races) {
        this.races = races;
    }
}

