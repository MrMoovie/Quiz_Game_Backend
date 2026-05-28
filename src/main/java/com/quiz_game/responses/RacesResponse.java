package com.quiz_game.responses;

import com.quiz_game.entities.RaceEntity;

import java.util.List;

public class RacesResponse extends BasicResponse {
    private final List<RaceEntity> races;

    public RacesResponse(boolean success, List<RaceEntity> races) {
        super(success, null);
        this.races = races;
    }

    public List<RaceEntity> getRaces() {
        return races;
    }
}

