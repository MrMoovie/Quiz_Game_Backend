package com.ashcollege.responses;

import com.ashcollege.entities.BidEntity;
import com.ashcollege.entities.PostEntity;

import java.util.List;

public class PostModel {
    private int id;
    private String text;
    private String area;
    private String fileLink;
    private String categoryName;
    private List<BidModel> bids;

    public PostModel () {
    }

    public PostModel (PostEntity postEntity, List<BidEntity> bidEntities) {
        this.text = postEntity.getText();
        this.area = postEntity.getArea();
        this.fileLink = postEntity.getFileLink();
        this.id = postEntity.getId();
        if (postEntity.getCategoryEntity() != null) {
            this.categoryName = postEntity.getCategoryEntity().getName();
        }
        if (bidEntities != null) {
            this.bids = bidEntities.stream().map(BidModel::new).toList();
        }
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {return id;}

    public List<BidModel> getBids() {
        return bids;
    }

    public void setBids(List<BidModel> bids) {
        this.bids = bids;
    }
}
