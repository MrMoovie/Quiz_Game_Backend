package com.quiz_game.entities;

import java.util.List;

import static com.quiz_game.utils.Constants.USER_TYPE_STUDENT;
import static com.quiz_game.utils.Constants.USER_TYPE_TEACHER;

public class TeacherEntity extends BasicUser{
    private List<RaceEntity> races;

    public List<RaceEntity> getRaces() {
        return races;
    }

    public void setRaces(List<RaceEntity> races) {
        this.races = races;
    }
    @Override
    public int getUserType(){
        return USER_TYPE_TEACHER;
    }
}
