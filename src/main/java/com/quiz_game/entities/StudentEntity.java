package com.quiz_game.entities;

import java.util.List;

public class StudentEntity extends BasicUser{
    List<GameEntity> gameHistory;

    public List<GameEntity> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<GameEntity> gameHistory) {
        this.gameHistory = gameHistory;
    }
}
