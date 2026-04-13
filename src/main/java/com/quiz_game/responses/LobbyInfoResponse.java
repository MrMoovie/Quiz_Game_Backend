package com.quiz_game.responses;

import com.quiz_game.entities.StudentEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyInfoResponse extends BasicResponse{
    private String teacherName;
    private List<Map<String, Object>> students = new ArrayList<>();
    public LobbyInfoResponse(boolean success, Integer errorCode, String teacherName, List<StudentEntity> students){
        super(success, errorCode);
        this.teacherName = teacherName;
        for(StudentEntity st : students){
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("name", st.getFullName());
//            stringObjectMap.put("trackId");
            this.students.add(stringObjectMap);
        }
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<Map<String, Object>> getStudents() {
        return students;
    }

    public void setStudents(List<Map<String, Object>> students) {
        this.students = students;
    }
}
