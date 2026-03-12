package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import com.quiz_game.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

import java.util.Collections;
import java.util.List;

import static com.quiz_game.utils.Constants.USER_TYPE_CLIENT;
import static com.quiz_game.utils.Errors.*;

@RestController
public class GameController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/get-all-students-in-race")
    public BasicResponse getAllStudents(String teacherToken,int raceId) {
        TeacherEntity teacherEntity = persist.getTeacherByToken(teacherToken);
        if (teacherEntity == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isTeacherHostingRace(teacherEntity, raceId)) {
            return new BasicResponse(false, ERROR_RACE_AND_TEACHER_DOESNT_MATCH);
        }
        List<StudentEntity> studenList = persist.getAllStudentsByRaceID(raceId);
        return new RaceStudentsResponse(true,null, studenList);
    }

    /// צריך ליצור פוקנציה שבודקת מה הסטטוס של המרוץ






}