package com.example.csye6225;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
public class Note {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")

    private String ID;

    private String title;

    @Column(columnDefinition = "VARCHAR(4096)")

    private String content;

    private int userID;

    private String created_on;
    private String last_updated_on;

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getUserID() {
        return userID;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getLast_updated_on() {
        return last_updated_on;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public void setLast_updated_on(String last_updated_on) {
        this.last_updated_on = last_updated_on;
    }
}