package com.quiz_game.responses;

import com.quiz_game.entities.RaceDTO;

import java.util.List;

public class RacesResponse extends BasicResponse {
    private final List<RaceDTO> races;

    public RacesResponse(boolean success, List<RaceDTO> races) {
        super(success, null);
        this.races = races;
    }

    public List<RaceDTO> getRaces() {
        return races;
    }
}

