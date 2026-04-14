package com.quiz_game.responses;

import com.quiz_game.entities.TrackEntity;

public class TrackResponse extends BasicResponse {
    private final TrackEntity track;
    public TrackResponse(TrackEntity trackEntity) {
        super(true,null);
        this.track = trackEntity;
    }

    public TrackEntity getTrack() {
        return track;
    }
}
