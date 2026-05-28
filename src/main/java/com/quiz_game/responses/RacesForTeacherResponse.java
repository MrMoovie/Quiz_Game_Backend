package com.quiz_game.responses;

import com.quiz_game.entities.RaceEntity;

import java.util.List;

public class RacesForTeacherResponse extends BasicResponse {
    private final List<RaceEntity> races;

    public RacesForTeacherResponse(boolean success, List<RaceEntity> races) {
        super(success, null);
        this.races = races;
    }

    public List<RaceEntity> getRaces() {
        return races;
    }
}

