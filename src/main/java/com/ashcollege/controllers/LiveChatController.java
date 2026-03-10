package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.service.Persist;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ashcollege.utils.Errors.ERROR_WRONG_CREDENTIALS;

@RestController
public class LiveChatController {

    @Autowired
    private Persist persist;

    private Map<String, List<SseEmitter>> subscribers = new ConcurrentHashMap<>();

    @PostConstruct
    public void init () {

    }

    private void sendMessage (boolean typing, BasicUser sender, BasicUser receiver, String message) {
        System.out.println("Sending message to token " + receiver.getToken());
        List<SseEmitter> sessions = this.subscribers.get(receiver.getToken());
        if (sessions != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", typing ? "0" : "1");
            jsonObject.put("content", message);
            jsonObject.put("sender", sender.getFullName());
            jsonObject.put("time", new Date().toString());
            System.out.println("Total sessions " + sessions.size());
            for (SseEmitter sseEmitter : sessions) {
                try {
                    sseEmitter.send(SseEmitter.event()
                                    .name(typing ? "typing" : "message")
                                    .id("message")
                            .data(jsonObject.toString()));
                    System.out.println("Success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @RequestMapping ("/subscribe")
    public SseEmitter subscribe (String token) {
        SseEmitter sseEmitter = new SseEmitter(10 * 60 * 1000L);
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
        return sseEmitter;
    }

    @RequestMapping("/send-message")
    public BasicResponse sendMessage(String token, String newMessage, int bidId) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        if (clientEntity != null) {
            BidEntity bidEntity = persist.loadObject(BidEntity.class, bidId);
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setBidEntity(bidEntity);
            messageEntity.setContent(newMessage);
            messageEntity.setClient(true);
            messageEntity.setCreationDate(new Date());
            persist.save(messageEntity);
            ProffesionalEntity receiver = bidEntity.getProffesionalEntity();
            sendMessage(false, clientEntity, receiver, newMessage);
            return new BasicResponse(true, null);
        } else {
            ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
            if (proffesionalEntity != null) {
                BidEntity bidEntity = persist.loadObject(BidEntity.class, bidId);
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setBidEntity(bidEntity);
                messageEntity.setContent(newMessage);
                messageEntity.setClient(false);
                messageEntity.setCreationDate(new Date());
                persist.save(messageEntity);
                ClientEntity receiver = bidEntity.getPostEntity().getClientEntity();
                sendMessage(false, proffesionalEntity, receiver, newMessage);
                return new BasicResponse(true, null);
            } else {
                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
            }
        }

    }

    @RequestMapping("/typing")
    public BasicResponse typing(String token, int bidId) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        if (clientEntity != null) {
            BidEntity bidEntity = persist.loadObject(BidEntity.class, bidId);
            ProffesionalEntity receiver = bidEntity.getProffesionalEntity();
            sendMessage(true, clientEntity, receiver, "");
            return new BasicResponse(true, null);
        } else {
            ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
            if (proffesionalEntity != null) {
                BidEntity bidEntity = persist.loadObject(BidEntity.class, bidId);
                ClientEntity receiver = bidEntity.getPostEntity().getClientEntity();
                sendMessage(true, proffesionalEntity, receiver, "");
                return new BasicResponse(true, null);
            }else {
                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
            }

        }
    }

}
