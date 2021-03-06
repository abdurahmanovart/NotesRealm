package com.github.abdurahmanovart.notesrealm.model;

import io.realm.RealmObject;

/**
 * @author Abdurakhmanov on 31.07.17
 */

public class Note extends RealmObject {

    private String id;

    private String title;

    private String body;

    public Note() {
        //Empty constructor needed by Realm
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}