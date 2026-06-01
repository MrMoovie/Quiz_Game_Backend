package com.quiz_game.responses;

import com.quiz_game.entities.StudentEntity;
import com.quiz_game.entities.TrackEntity;

import java.util.List;

public class RaceStudentsResponse  extends  BasicResponse{
    private final List<StudentEntity> students;
    private final List<TrackEntity> tracks;
    private int goalScore;



    public RaceStudentsResponse(boolean success, Integer errorCode, List<StudentEntity> students, List<TrackEntity> tracks, int goalScore) {
        super(success, errorCode);
        this.students = students;
        this.tracks = tracks;
        this.goalScore = goalScore;

    }

    public int getGoalScore() {
        return goalScore;
    }

    public void setGoalScore(int goalScore) {
        this.goalScore = goalScore;
    }

    public List<StudentEntity> getStudents() {
        return students;
    }

    public List<TrackEntity> getTracks() {
        return tracks;
    }
}
