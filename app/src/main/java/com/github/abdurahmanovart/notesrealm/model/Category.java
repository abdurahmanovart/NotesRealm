package com.github.abdurahmanovart.notesrealm.model;

import io.realm.RealmObject;

/**
 * @author Abdurakhmanov on 29.07.17
 */

public class Category extends RealmObject {

    private String categoryName;

    public Category() {
        //Empty constructor needed by realm
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
