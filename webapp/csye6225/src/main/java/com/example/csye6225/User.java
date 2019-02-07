package com.example.csye6225;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//import org.springframework.data.annotation.Id;
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String name;
    private String password;
    private String realpassword;

    public User(){  }

    public String getName() {
        return name;
    }
    public String getpassword() { return password; }
    public int getID() {
        return ID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setaddress(String password) {
        this.password = password;
    }
    public String getRealpassword() {
        return realpassword;
    }
    public void setRealpassword(String realpassword) {
        this.realpassword = realpassword;
    }
}
