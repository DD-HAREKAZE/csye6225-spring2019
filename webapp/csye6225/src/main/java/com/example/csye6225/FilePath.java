
package com.example.csye6225;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FilePath {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String ID;

    private String path;

    private String noteID;

    private String filename;

    public String getID() {
        return ID;
    }

    public String getPath() {
        return path;
    }

    public String getNoteID() {
        return noteID;
    }

    public String getFilename() {
        return filename;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

