package com.ashcollege.entities;

public class PostEntity extends BaseEntity {
    private ClientEntity clientEntity;
    private String text;
    private String fileLink;
    private String area;
    private CategoryEntity categoryEntity;

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}
