package com.ashcollege.responses;

import com.ashcollege.entities.BidEntity;
import com.ashcollege.entities.MessageEntity;

import java.util.List;

public class BidResponse extends BasicResponse {
    private String description;
    private float proposedPrice;
    private List<MessageModel> conversation;

    public BidResponse (boolean success, Integer errorCode, BidEntity bidEntity, List<MessageEntity> messageEntities) {
        super(success, errorCode);
        this.description = bidEntity.getDescription();
        this.proposedPrice = bidEntity.getProposedPrice();
        this.conversation = messageEntities.stream().map(MessageModel::new).toList();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(float proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public List<MessageModel> getConversation() {
        return conversation;
    }

    public void setConversation(List<MessageModel> conversation) {
        this.conversation = conversation;
    }
}
