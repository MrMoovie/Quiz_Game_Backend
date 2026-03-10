package com.ashcollege.entities;

public class ProffesionalCategoryEntity extends BaseEntity {
    private ProffesionalEntity proffesionalEntity;
    private CategoryEntity categoryEntity;

    public ProffesionalEntity getProffesionalEntity() {
        return proffesionalEntity;
    }

    public void setProffesionalEntity(ProffesionalEntity proffesionalEntity) {
        this.proffesionalEntity = proffesionalEntity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}
