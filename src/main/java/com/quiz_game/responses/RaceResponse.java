package com.quiz_game.responses;

import com.quiz_game.entities.RaceEntity;

public class RaceResponse extends BasicResponse{
    private final RaceEntity race;
    public RaceResponse(RaceEntity race) {
        super(true,null);
        this.race = race;
    }

    public RaceEntity getRace() {
        return race;
    }
}
