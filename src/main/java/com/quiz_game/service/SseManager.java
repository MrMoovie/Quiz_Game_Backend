package com.quiz_game.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@Service
public class SseManager {

    @PostConstruct
    public void init(){

    }

    private final Map<String, List<SseEmitter>> subscribers = new ConcurrentHashMap<>();

    @RequestMapping("/subscribe")
    public SseEmitter subscribe (String token) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> {
            this.subscribers.get(token).remove(sseEmitter);
        });
        sseEmitter.onError((event) -> {
            this.subscribers.get(token).remove(sseEmitter);
        });
        List<SseEmitter> currentEmitters = this.subscribers.get(token);
        if (currentEmitters == null) {
            currentEmitters = new CopyOnWriteArrayList<>();
            this.subscribers.put(token, currentEmitters);
        }
        currentEmitters.add(sseEmitter);

        System.out.println("[T] Added Emitter");

        return sseEmitter;
    }


    public void studentHasJoined(String teacherToken, String studentName, int trackId){
        List<SseEmitter> sessions = subscribers.get(teacherToken);
        if(sessions!=null && !sessions.isEmpty()){
            JSONObject json = new JSONObject();
            json.put("event", "STUDENT_JOIN");
            json.put("studentName", studentName);
            json.put("trackId", trackId);

            sendEvent(sessions, "lobby-update", json.toString());

            System.out.println("[S] updated teacher");
        }

    }

    public void scoreEvent(String teacherToken, int studentId, int scoreEarned, int newPosition){
        List<SseEmitter> sessions = subscribers.get(teacherToken);
        if(sessions!=null && !sessions.isEmpty()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event","EARNED_POINTS");
            jsonObject.put("id", studentId);
            jsonObject.put("score", scoreEarned);
            jsonObject.put("position", newPosition);

            sendEvent(sessions, "score-update", jsonObject.toString());
        }


    }

    public void sendEvent(List<SseEmitter> sessions, String eventName, String data){
        for(SseEmitter emitter:sessions){
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                System.out.println("failed sending event, ERROR:\n" + e);
            }
        }
    }

    public void gameStarted(List<String> tokens, int raceId){
        for(String token: tokens){
            List<SseEmitter> sessions = subscribers.get(token);
            if(sessions!=null && !sessions.isEmpty()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("event","GAME_STARTED");
                jsonObject.put("isStarted", true);
                jsonObject.put("raceId", raceId);
                sendEvent(sessions, "game-started", jsonObject.toString());
            }
        }
    }



}
