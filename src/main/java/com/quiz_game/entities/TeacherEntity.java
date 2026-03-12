package com.quiz_game.entities;

import java.util.List;

public class TeacherEntity extends BasicUser{
    private List<RaceEntity> races;

    public List<RaceEntity> getSessions() {
        return races;
    }

    public void setSessions(List<RaceEntity> sessions) {
        this.races = sessions;
    }
}
