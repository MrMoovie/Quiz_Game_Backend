package com.quiz_game.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Service
public class SseManager {

    // The map key is now Integer (raceId), mapping to an inner Map of String (token) -> SseEmitter
    private final Map<Integer, Map<String, SseEmitter>> raceSubscribers = new ConcurrentHashMap<>();


    public SseEmitter subscribe(@RequestParam String token, @RequestParam Integer raceId) {

        // 1. Get or create the room (Map of token -> SseEmitter)
        Map<String, SseEmitter> roomSubscribers = raceSubscribers.computeIfAbsent(raceId, k -> new ConcurrentHashMap<>());

        // 2. Check if an emitter already exists for this token in this race
        if (roomSubscribers.containsKey(token)) {
            System.out.println("[SSE] Returning existing emitter for token in Race ID: " + raceId);
            return roomSubscribers.get(token);
        }

        // 3. Create a new emitter if it doesn't exist
        SseEmitter sseEmitter = new SseEmitter(0L); // 0L keeps connection open infinitely

        // Pass the token to the removal method instead of the emitter object
        sseEmitter.onCompletion(() -> removeEmitter(raceId, token));
        sseEmitter.onError((event) -> removeEmitter(raceId, token));
        sseEmitter.onTimeout(() -> removeEmitter(raceId, token));

        // Store the new emitter mapped to the token
        roomSubscribers.put(token, sseEmitter);

        System.out.println("[SSE] New user subscribed to Race ID: " + raceId);

        return sseEmitter;
    }

    private void removeEmitter(Integer raceId, String token) {
        Map<String, SseEmitter> roomSubscribers = raceSubscribers.get(raceId);
        if (roomSubscribers != null) {
            roomSubscribers.remove(token); // Remove just this user's connection
            if (roomSubscribers.isEmpty()) {
                raceSubscribers.remove(raceId); // Clean up empty rooms
            }
        }
    }

    // Broadcast to EVERYONE in the race lobby
    public void studentHasJoined(int raceId, String studentName, int trackId) {
        Map<String, SseEmitter> sessions = raceSubscribers.get(raceId);
        if (sessions != null && !sessions.isEmpty()) {
            JSONObject json = new JSONObject();
            json.put("event", "STUDENT_JOIN");
            json.put("studentName", studentName);
            json.put("trackId", trackId);

            sendEvent(sessions, "lobby-update", json.toString());
            System.out.println("[SSE] Broadcasted student join to Race ID: " + raceId);
        }
    }

    // Broadcast score updates to EVERYONE in the game
    public void scoreEvent(int raceId, int studentId, int scoreEarned, int newPosition) {
        Map<String, SseEmitter> sessions = raceSubscribers.get(raceId);
        if (sessions != null && !sessions.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", "EARNED_POINTS");
            jsonObject.put("id", studentId);
            jsonObject.put("score", scoreEarned);
            jsonObject.put("position", newPosition);

            System.out.println("score earned "+scoreEarned);
            sendEvent(sessions, "score-update", jsonObject.toString());
        }
    }

    // Broadcast game start to EVERYONE in the room
    public void gameStarted(int raceId) {
        Map<String, SseEmitter> sessions = raceSubscribers.get(raceId);
        if (sessions != null && !sessions.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", "GAME_STARTED");
            jsonObject.put("isStarted", true);
            jsonObject.put("raceId", raceId);

            sendEvent(sessions, "game-started", jsonObject.toString());
            System.out.println("[SSE] Broadcasted game start to Race ID: " + raceId);
        }
    }

    // Helper method to iterate over the map's values and send the data
    private void sendEvent(Map<String, SseEmitter> sessions, String eventName, String data) {
        for (SseEmitter emitter : sessions.values()) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                // If an emitter is dead, remove it so we don't try again
                emitter.complete();
            }
        }
    }
}