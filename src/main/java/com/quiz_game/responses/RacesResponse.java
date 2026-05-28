package com.quiz_game.responses;

import com.quiz_game.entities.RaceDTO;
import com.quiz_game.entities.RaceEntity;

import java.util.ArrayList;
import java.util.List;

public class RacesResponse extends BasicResponse {
    private final List<RaceDTO> races = new ArrayList<>();

    public RacesResponse(boolean success, List<RaceEntity> races) {
        super(success, null);
        for(RaceEntity race : races){
            RaceDTO raceDTO = new RaceDTO(race.getTeacher(), race.getCapacity(), race.getStatus(), race.getTracks());
            this.races.add(raceDTO);
        }
    }

    public List<RaceDTO> getRaces() {
        return races;
    }
}

