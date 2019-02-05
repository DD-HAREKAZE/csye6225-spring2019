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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                if((singleRecord.getName().equals(iusername))&&BCrypt.checkpw(ipassword,singleRecord.getpassword() )){
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

        // register function
        @RequestMapping(value = "/user/register",method = RequestMethod.POST)
        public String RegisterNewUser(@RequestHeader String username,@RequestHeader String password){
            String result=" ";
            int code=0;
            //0 means OK
            //1 means occupied
            //2 means username is not a email address
            //3 means password is not strong enough

            //username=email? check
            if(isEmail(username)==false){
                String p="Error: username must be an email address!";
                return p;
            }

            //password strong? check
            if(password.length()<=8){
                return "The length of password should be over 8.";
            }
            String reg = "[A-Z]";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(password);
            int j=0;
            while(matcher.find()){
                j++;
            }
            if(j==0){
                return "You need at least one upper case char in password.";
            }

            String reg2= "[a-z]";
            Pattern pattern1 = Pattern.compile(reg2);
            Matcher matcher1 = pattern1.matcher(password);
            int k=0;
            while(matcher1.find()){
                k++;
            }
            if(k==0){
                return "You need at least one lower case char in password.";
            }

            String reg3 = "[0-9]";
            Pattern pattern2 = Pattern.compile(reg3);
            Matcher matcher2 = pattern2.matcher(password);
            int l=0;
            while(matcher2.find()){
                l++;
            }
            if(l==0){
                return "You need at least one number in password.";
            }




            List<User> a=userRepository.findAll();
            for(User singleRecord:a){
                if(singleRecord.getName().equals(username)){code=1;}
            }
            if(code==1){
                result="Sorry, this account already exists!";
                return result;
            }
            if(code==0){
                User temp=new User();
                temp.setName(username);
                String codepass =  BCrypt.hashpw(password, BCrypt.gensalt());
                temp.setPassword(codepass);
                temp.setRealpassword(password);
                userRepository.save(temp);
                result="Register success! Your username is: "+temp.getName()+" and your password is: "+temp.getRealpassword();
            }

            return result;
        }
}





