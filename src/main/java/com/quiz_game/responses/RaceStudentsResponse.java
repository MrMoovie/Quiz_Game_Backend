package com.quiz_game.responses;

import com.quiz_game.entities.BidEntity;
import com.quiz_game.entities.PostEntity;
import com.quiz_game.entities.StudentEntity;

import java.util.List;

public class RaceStudentsResponse  extends  BasicResponse{
    private final List<StudentEntity> students;


    public RaceStudentsResponse(boolean success, Integer errorCode, List<StudentEntity> students) {
        super(success, errorCode);
        this.students = students;

    }

    public List<StudentEntity> getStudents() {
        return students;
    }

}
