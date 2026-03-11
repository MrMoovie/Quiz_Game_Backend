package com.quiz_game.entities;

import java.util.List;

public class StudentEntity extends BasicUser{
    List<RaceEntity> gameHistory;

    public List<RaceEntity> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<RaceEntity> gameHistory) {
        this.gameHistory = gameHistory;
    }
}
