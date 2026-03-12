package com.quiz_game.entities;

import java.util.List;

public class TeacherEntity extends BasicUser{
    private List<RaceEntity> races;

    public List<RaceEntity> getRaces() {
        return races;
    }

    public void setRaces(List<RaceEntity> races) {
        this.races = races;
    }
}
