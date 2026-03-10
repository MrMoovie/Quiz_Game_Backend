package com.ashcollege.responses;
import com.ashcollege.entities.CategoryEntity;


import java.util.List;

public class CategoriesResponse extends BasicResponse {
    private List<CategoryModel> categories;

    public CategoriesResponse() {
    }

    public CategoriesResponse(boolean success, Integer errorCode, List<CategoryEntity> categories) {
        super(success, errorCode);
        this.categories = categories.stream().map(CategoryModel::new).toList();
    }


    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> posts) {
        this.categories = posts;
    }
}

