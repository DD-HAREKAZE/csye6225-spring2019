package com.example.csye6225.controller;

import com.example.csye6225.entities.User;
import com.example.csye6225.dao.UserRepository;
import com.example.csye6225.helpers.BCrypt;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

public class UserController {
    @Autowired
    private UserRepository userRepository;

    // assignment1 GET method:/ return current time
    //redo: add basic auth
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String getTime(HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();
        String header = request.getHeader("Authorization");
        String basicAuthEncoded = header.substring(6);
        String basicAuthAsString = new String(Base64.getDecoder().decode(basicAuthEncoded.getBytes()));
        final String[] credentialValues = basicAuthAsString.split(":", 2);

        String username = credentialValues[0];
        String password = credentialValues[1];

        Optional<User> u = userRepository.findById(username);
        User user = u.isPresent() ? u.get() : null;

        if (user != null) {
            if (BCrypt.checkpw(password, user.getpassword())) {
                //output current time
                jsonObject.addProperty("User: ", username);
                jsonObject.addProperty("Current time: ", new Date().toString());
                jsonObject.addProperty("Status: ", "200");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonObject.addProperty("Error: ", "password is not right");
                jsonObject.addProperty("Status:", "400");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            jsonObject.addProperty("Error: ", "You have not registered.");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


        return jsonObject.toString();
    }

    // register function
    @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = "application/json")
    public String RegisterNewUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();
        Optional<User> u = userRepository.findById(user.getName());
        if (u.isPresent()) {
            jsonObject.addProperty("Error message:", "Sorry, this account already exists!");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        String username = user.getName();
        String password = user.getpassword();

        //username=email? check
        if (isEmail(username) == false) {
            jsonObject.addProperty("Error message:", "Username is not an email address.");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        //password strong? check
        if (password.length() <= 8) {
            jsonObject.addProperty("Error message:", "Password length should be over 8.");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        String reg = "[A-Z]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(password);
        int j = 0;
        while (matcher.find()) {
            j++;
        }
        if (j == 0) {
            jsonObject.addProperty("Error message:", "You need at least one upper case char in password.");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();

        }

        String reg2 = "[a-z]";
        Pattern pattern1 = Pattern.compile(reg2);
        Matcher matcher1 = pattern1.matcher(password);
        int k = 0;
        while (matcher1.find()) {
            k++;
        }
        if (k == 0) {
            jsonObject.addProperty("Error message:", "You need at least one lower case char in password.");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        String reg3 = "[0-9]";
        Pattern pattern2 = Pattern.compile(reg3);
        Matcher matcher2 = pattern2.matcher(password);
        int l = 0;
        while (matcher2.find()) {
            l++;
        }
        if (l == 0) {
            jsonObject.addProperty("Error message:", "You need at least one number in password.");
            jsonObject.addProperty("Status:", "400");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.save(user);
        jsonObject.addProperty("Message:", "Register success!");
        jsonObject.addProperty("Your username:", username);
        jsonObject.addProperty("Your password:", password);
        jsonObject.addProperty("Status:", "201");
        response.setStatus(HttpServletResponse.SC_CREATED);
        return jsonObject.toString();

    }

    //check if a username is an email address or not
    public static boolean isEmail(String string) {
        if (string == null) return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches()) return true;
        else
            return false;
    }
}

