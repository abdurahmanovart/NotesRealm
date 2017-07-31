package com.github.abdurahmanovart.notesrealm.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * @author Abdurakhmanov on 29.07.17
 */

public class Category extends RealmObject {

    private String categoryName;
    private RealmList<Note> notes;

    public Category() {
        //Empty constructor needed by realm
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Note> notes) {
        this.notes = notes;
    }
}
