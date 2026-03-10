package com.ashcollege.entities;

public class MessageEntity extends BaseEntity {
    private BidEntity bidEntity;
    private String content;
    private int status; // 0 - unread, 1 - delivered, 2 - read
    private boolean client;

    public BidEntity getBidEntity() {
        return bidEntity;
    }

    public void setBidEntity(BidEntity bidEntity) {
        this.bidEntity = bidEntity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }
}
