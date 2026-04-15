package com.quiz_game.entities;

import java.util.List;

import static com.quiz_game.utils.Constants.USER_TYPE_STUDENT;

public class StudentEntity extends BasicUser{
    List<TrackEntity> gameHistory;

    public List<TrackEntity> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<TrackEntity> gameHistory) {
        this.gameHistory = gameHistory;
    }
    @Override
    public int getUserType(){
        return USER_TYPE_STUDENT;
    }
}
