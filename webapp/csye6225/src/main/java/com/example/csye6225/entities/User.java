package com.example.csye6225.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity

public class User {
    @Id
    private String name;
    private String password;

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getpassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setaddress(String password) {
        this.password = password;
    }
}
