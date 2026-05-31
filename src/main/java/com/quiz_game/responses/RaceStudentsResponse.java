package com.quiz_game.responses;

import com.quiz_game.entities.StudentDTO;

import java.util.List;

public class RaceStudentsResponse  extends  BasicResponse{
    private final List<StudentDTO> students;
    private int goalScore;



    public RaceStudentsResponse(boolean success, Integer errorCode, List<StudentDTO> students, int goalScore) {
        super(success, errorCode);
        this.students = students;
        this.goalScore = goalScore;

    }

    public int getGoalScore() {
        return goalScore;
    }

    public void setGoalScore(int goalScore) {
        this.goalScore = goalScore;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

}
