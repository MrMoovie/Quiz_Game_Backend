package com.quiz_game.entities;

import java.util.List;

public class StudentEntity extends BasicUser{
    List<TrackEntity> gameHistory;

    public List<TrackEntity> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<TrackEntity> gameHistory) {
        this.gameHistory = gameHistory;
    }
}
