package com.example.csye6225.helpers;

import com.example.csye6225.dao.UserRepository;
import com.example.csye6225.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Helper {

    @Autowired
    private UserRepository userRepository;

    public int getUserID(String header) {
        //This is the logic to fetch user password from the authorization header value by removing "Basic" keyword, decoding and splitting with :

        if (header != null && header.contains("Basic")) {
            String basicAuthEncoded = header.substring(6);
            String basicAuthAsString = new String(Base64.getDecoder().decode(basicAuthEncoded.getBytes()));


            final String[] credentialValues = basicAuthAsString.split(":", 2);
            //If user exists in DB , return the user object.
            User user = validateUser(credentialValues[0], credentialValues[1]);
            if (user != null) {
                return user.getID();
            }
        }

        return -1;

    }


    public User validateUser(String username, String password) {
        for (User user : userRepository.findAll()) {
            if ((user.getName().equals(username)) && BCrypt.checkpw(password, user.getpassword()))
                return user;

        }
        return null;
    }
}
