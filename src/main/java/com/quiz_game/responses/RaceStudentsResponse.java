package com.quiz_game.responses;

import com.quiz_game.entities.StudentDTO;

import java.util.List;

public class RaceStudentsResponse  extends  BasicResponse{
    private final List<StudentDTO> students;



    public RaceStudentsResponse(boolean success, Integer errorCode, List<StudentDTO> students) {
        super(success, errorCode);
        this.students = students;

    }

    public List<StudentDTO> getStudents() {
        return students;
    }

}
