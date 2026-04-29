package com.quiz_game.controllers;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.quiz_game.entities.RaceEntity;
import com.quiz_game.entities.StudentEntity;
import com.quiz_game.entities.TeacherEntity;
import com.quiz_game.entities.TrackEntity;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import com.quiz_game.service.SseManager;
import com.quiz_game.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.List;

import static com.quiz_game.utils.Constants.*;
import static com.quiz_game.utils.Errors.*;

@RestController
public class PreGameController {
    @Autowired
    private Persist persist;

    @Autowired
    private SseManager sseManager;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("lobby-info")
    public BasicResponse getLobbyInfo(Integer raceId, String token) {
        StudentEntity student = persist.getStudentByToken(token);
        TeacherEntity teacher = persist.getTeacherByToken(token);
        if (student == null && teacher == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        RaceEntity race = persist.getRaceByRaceId(raceId);
        if (race == null) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        List<StudentEntity> studentsInRace = persist.getAllStudentsByRaceID(race.getId());
        return new LobbyInfoResponse(true, null, race.getTeacher().getFullName(), studentsInRace);
    }

    @RequestMapping("/create-race")
    public BasicResponse createRace(String token) {
        TeacherEntity teacher = persist.getTeacherByToken(token);
        if (teacher != null) {
            String entryCode = GeneralUtils.generateOtp();
            RaceEntity race = new RaceEntity();
            race.setTeacher(teacher);
            race.setEntryCode(entryCode);
            race.setCapacity(0);
            race.setStatus(RACE_STATUS_LOBBY);
            persist.save(race);
            //HAS TO SUBSCRIBE
            return new CreateRaceResponse(true, null, race.getId(), entryCode);
        } else {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
    }


    @RequestMapping("/join-race")
    public BasicResponse joinRace(String token, String entryCode) {
        StudentEntity student = persist.getStudentByToken(token);
        boolean isStudentInRace = false;
        if (student == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (persist.isStudentInAnyNonFinishedRace(student)) {
            RaceEntity race = persist.getRaceByEntryCode(entryCode);
            isStudentInRace = persist.isStudentInSpecificRace(student, race.getId());
            if(!isStudentInRace) {
                return new BasicResponse(false, ERROR_ALREADY_HAVE_AN_OPEN_RACE);
            }
        }
        RaceEntity race = persist.getRaceByEntryCode(entryCode.trim());
        if (race == null || entryCode.trim().isEmpty()) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        if (race.getCapacity() > 8) { // MAX CAPACITY = 8
            return new BasicResponse(false, ERROR_RACE_IS_FULL);
        }
        if (race.getStatus() == RACE_STATUS_LOBBY) {
            if(!isStudentInRace) {
                TrackEntity track = new TrackEntity();
                track.setRace(race);
                track.setStudent(student);
                persist.save(track);
                race.setCapacity(race.getCapacity() + 1);
                persist.save(race);
//          sseManager.studentHasJoined(race.getTeacher().getToken(), student.getFullName(), track.getId());
                sseManager.studentHasJoined(race.getId(), student.getFullName(), track.getId());
            }

            //HAS TO SUBSCRIBE
            return new JoinRaceResponse(true, null, race.getId());
        } else {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
    }



    @RequestMapping("/get-all-races")
    public BasicResponse getAllRaces(String token) {
        StudentEntity student = persist.getStudentByToken(token);
        if (student != null) {
            return new RacesResponse(true, null, persist.getAllRaces());
        } else {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
    }

    @RequestMapping("/get-all-teacher-races")
    public BasicResponse getAllTeacherRaces(String token) {
        TeacherEntity teacher = persist.getTeacherByToken(token);
        if (teacher != null) {
            return new RacesResponse(true, null, persist.getRacesByTeacherId(teacher.getId()));
        } else {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
    }
    @RequestMapping("/get-race")
    public BasicResponse getRace(int raceId) {
        RaceEntity race = persist.getRaceByRaceId(raceId);
        if (race == null) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        return new RaceResponse(race);
    }

    @RequestMapping("/start-race")
    public BasicResponse startRace(String token, int raceId) {
        TeacherEntity teacher = persist.getTeacherByToken(token);
        RaceEntity race = persist.getRaceByRaceId(raceId);
        if (teacher == null || race == null) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (!persist.isTeacherHostingRace(teacher, raceId)) {
            return new BasicResponse(false, ERROR_UNKNOWN_RACE_FOR_TEACHER);
        }
//        if (persist.isAnyRaceOpenForTeacher(teacher)) {
//            return new BasicResponse(false, ERROR_ALREADY_HAVE_AN_OPEN_RACE);
//        }
        if (race.getStatus() == RACE_STATUS_LOBBY) {

            race.setStatus(RACE_STATUS_STARTED);
            persist.save(race);

//          List<String> studentTokens = persist.getAllStudentsByRaceID(race.getId()).stream().map(StudentEntity::getToken).toList();
//          sseManager.gameStarted(studentTokens, race.getId());
            sseManager.gameStarted(race.getId());

            return new BasicResponse(true, null);

        } else {
            return new BasicResponse(false, ERROR_RACE_CANT_BE_STARTED);
        }
    }
}

