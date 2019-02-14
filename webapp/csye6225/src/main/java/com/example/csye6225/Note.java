package com.example.csye6225;

import javax.persistence.*;


@Entity
public class Note {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int ID;

    private String title;

    @Column(columnDefinition = "VARCHAR(4096)")
    private String description;

    private int userID;

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getUserID() {
        return userID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}