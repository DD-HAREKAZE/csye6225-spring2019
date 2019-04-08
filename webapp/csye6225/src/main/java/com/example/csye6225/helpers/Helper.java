package com.example.csye6225.helpers;

import com.example.csye6225.dao.UserRepository;
import com.example.csye6225.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class Helper {

    @Autowired
    private UserRepository userRepository;


    public String validateUser(String header) {

        String basicAuthEncoded = header.substring(6);
        String basicAuthAsString = new String(Base64.getDecoder().decode(basicAuthEncoded.getBytes()));
        final String[] credentialValues = basicAuthAsString.split(":", 2);

        String username = credentialValues[0];
        String password = credentialValues[1];

        Optional<User> u = userRepository.findById(username);
        User user = u.isPresent() ? u.get() : null;

        if (user != null) {
            if (BCrypt.checkpw(password, user.getpassword())) {
                return username;
            } else {
                return "0";
            }

        } else {
            return "-1";
        }

    }
}
