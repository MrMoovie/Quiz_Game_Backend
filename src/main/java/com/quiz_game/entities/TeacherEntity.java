package com.quiz_game.entities;

import java.util.List;

public class TeacherEntity extends BasicUser{
    private List<RaceEntity> sessions;

    public List<RaceEntity> getSessions() {
        return sessions;
    }

    public void setSessions(List<RaceEntity> sessions) {
        this.sessions = sessions;
    }
}
