package com.quiz_game.controllers;

import com.quiz_game.entities.RaceEntity;
import com.quiz_game.entities.StudentEntity;
import com.quiz_game.entities.TeacherEntity;
import com.quiz_game.entities.TrackEntity;
import com.quiz_game.responses.BasicResponse;
import com.quiz_game.responses.CreateRaceResponse;
import com.quiz_game.responses.JoinRaceResponse;
import com.quiz_game.responses.RacesResponse;
import com.quiz_game.service.Persist;
import com.quiz_game.service.SseManager;
import com.quiz_game.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

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

    @RequestMapping("/create-race")
    public BasicResponse createRace(String token) {
        TeacherEntity teacher = persist.getTeacherByToken(token);
        if (teacher != null) {
            String entryCode = GeneralUtils.generateOtp();
            RaceEntity race = new RaceEntity();
            race.setTeacher(teacher);
            race.setEntryCode(entryCode);
            race.setOpen(true);
            race.setStatus(0);
            persist.save(race);
            return new CreateRaceResponse(true, null, race.getId(), entryCode);
        } else {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
    }



    @RequestMapping("/join-race")
    public BasicResponse joinRace(String token, String entryCode) {
        StudentEntity student = persist.getStudentByToken(token);
        if (student != null) {
            if (entryCode != null && !entryCode.trim().isEmpty()) {
                RaceEntity race = persist.getRaceByEntryCode(entryCode.trim());
                if (race != null && race.isOpen()) {
                    TrackEntity track = new TrackEntity();
                    track.setRace(race);
                    track.setStudent(student);
                    persist.save(track);

                    sseManager.studentHasJoined(race.getTeacher().getToken(), student.getFullName(), track.getId());

                    return new JoinRaceResponse(true, null, race.getId());
                } else {
                    return new BasicResponse(false, ERROR_MISSING_VALUES);
                }
            } else {
                return new BasicResponse(false, ERROR_MISSING_VALUES);
            }
        } else {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
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

    @RequestMapping("/start-race")
    public BasicResponse startRace(){


        return null;
    }




}

