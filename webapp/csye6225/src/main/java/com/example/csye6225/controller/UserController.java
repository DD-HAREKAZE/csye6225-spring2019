package com.example.csye6225.controller;


import com.example.csye6225.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController

@RequestMapping("/testboot")

public class UserController {
        @Autowired
        private UserRepository userRepository;
        //test function: show all users
        @RequestMapping("/getAllStudent")
        public List<User> getAllStudent(){
            return userRepository.findAll();
        }

        // assignment1:/GET current time
        @RequestMapping(value = "/GET",method = RequestMethod.GET)
//        public String getTime(HttpServletRequest request,@RequestHeader HttpHeaders headers){
        public String getTime(@RequestHeader String username,@RequestHeader String password){
            String s=" ";
//            String iusername = request.getHeader("username");
//            String ipassword = request.getHeader("password");


            String iusername = username;
            String ipassword = password;

            List<User> a=userRepository.findAll();
//            s=iusername+ipassword;
            int authorizeTag=0;//1 means authorized
            for (User singleRecord:a
                 ) {
                //System.out.println("Now identifying "+singleRecord.getName()+","+singleRecord.getpassword());
                if((singleRecord.getName().equals(iusername))&&singleRecord.getpassword().equals(ipassword) ){
                    authorizeTag=100;
                    //output current time
                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String hehe = dateFormat.format( now );


                    s="Welcome log in, "+iusername+"! Current time is: "+hehe;
                    break;
                }
            }
            if(authorizeTag==0){
                s="You are not currently logged in. You need to log in first.";
            }
            return s;
        }

        // register function
        @RequestMapping(value = "/user/register",method = RequestMethod.POST)
        public String RegisterNewUser(@RequestHeader String username,@RequestHeader String password){
            String result=" ";
            int occupied=0;
            List<User> a=userRepository.findAll();
            for(User singleRecord:a){
                if(singleRecord.getName().equals(username)){occupied=1;}

            }
            if(occupied==1){
                result="Sorry, this account already exists!";
                return result;
            }
            if(occupied==0){
                User temp=new User();
                temp.setName(username);
                temp.setPassword(password);
                userRepository.save(temp);
                result="Register success! Your username is: "+temp.getName()+" and your password is: "+temp.getpassword();
            }

            return result;
        }
}





