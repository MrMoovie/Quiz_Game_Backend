package com.ashcollege.entities;

public class BidEntity extends BaseEntity {
    private ProffesionalEntity proffesionalEntity;
    private PostEntity postEntity;
    // 0 - pending, 1 - accepted, 2 - rejected, 3 - in progress
    private int status;
    private float proposedPrice;
    private String description;

    public ProffesionalEntity getProffesionalEntity() {
        return proffesionalEntity;
    }

    public void setProffesionalEntity(ProffesionalEntity proffesionalEntity) {
        this.proffesionalEntity = proffesionalEntity;
    }

    public PostEntity getPostEntity() {
        return postEntity;
    }

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(float proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
