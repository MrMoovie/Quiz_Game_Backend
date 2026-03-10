package com.ashcollege.responses;

import com.ashcollege.entities.MessageEntity;

public class MessageModel {
    private int id;
    private String message;
    private String sender;
    private String receiver;
    private String time;

    public MessageModel() {
    }

    public MessageModel(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.message = messageEntity.getContent();
        if (messageEntity.isClient()) {
            this.sender = messageEntity.getBidEntity().getPostEntity().getClientEntity().getFullName();
            this.receiver = messageEntity.getBidEntity().getProffesionalEntity().getFullName();
        } else {
            this.sender = messageEntity.getBidEntity().getProffesionalEntity().getFullName();
            this.receiver = messageEntity.getBidEntity().getPostEntity().getCategoryEntity().getName();
        }
        this.time = messageEntity.getCreationDate().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
