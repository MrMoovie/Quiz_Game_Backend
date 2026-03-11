package com.quiz_game.entities;

import java.util.List;

public class TeacherEntity extends BasicUser{
    List<SessionEntity> sessionHistory;

    public List<SessionEntity> getSessionHistory() {
        return sessionHistory;
    }

    public void setSessionHistory(List<SessionEntity> sessionHistory) {
        this.sessionHistory = sessionHistory;
    }
}
