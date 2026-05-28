package com.quiz_game.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class RaceDTO extends BaseEntity{
    @JsonIgnore // infinite loop
    private final TeacherEntity teacher;
    private final int capacity;
    private final int status;
    private final List<TrackEntity> tracks;
    // 0-LOBBY 1-RUNNING 2-FINISHED


    public RaceDTO(TeacherEntity teacher, int capacity, int status, List<TrackEntity> tracks) {
        this.teacher = teacher;
        this.capacity = capacity;
        this.status = status;
        this.tracks = tracks;
    }
}
