package com.ashcollege.responses;

import com.ashcollege.entities.BidEntity;

public class BidModel {
    private int id;
    private String description;
    private float proposedPrice;
    private int status;
    private int postId;

    public BidModel() {
    }

    public BidModel (BidEntity bidEntity) {
        this.id = bidEntity.getId();
        this.description = bidEntity.getDescription();
        this.proposedPrice = bidEntity.getProposedPrice();
        this.status = bidEntity.getStatus();
        if (bidEntity.getPostEntity() != null) {
            this.postId = bidEntity.getPostEntity().getId();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
