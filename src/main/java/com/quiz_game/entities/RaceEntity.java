package com.quiz_game.entities;

import java.util.ArrayList;
import java.util.List;

public class RaceEntity extends BaseEntity{
    private TeacherEntity teacher;
    private String entryCode;
    private boolean isOpen;
    private int maxCapacity;
    private int status;
    private List<TrackEntity> tracks = new ArrayList<>();
    // 0-LOBBY 1-RUNNING 2-FINISHED

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public List<TrackEntity> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackEntity> tracks) {
        this.tracks = tracks;
    }


}
