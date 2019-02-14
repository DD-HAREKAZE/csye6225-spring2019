package com.example.csye6225.controller;


import com.example.csye6225.Note;
import com.example.csye6225.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

@RequestMapping("/testboot")

public class UserController {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private NoteRepository noteRepository;

        //test function: show all users
        @RequestMapping("/getAllStudent")
        public List<User> getAllStudent(){
            return userRepository.findAll();
        }




        // assignment1 GET method:/ return current time
        //redo: add basic auth
        @RequestMapping(value = "/",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String getTime(@RequestHeader String username, @RequestHeader String password) throws JSONException {
            String s=" ";
            JSONObject jsonObject = new JSONObject();
            String iusername = username;
            String ipassword = password;

            List<User> a=userRepository.findAll();
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
                    jsonObject.put("User: ",iusername);
                    jsonObject.put("Current time: ",hehe);
                    jsonObject.put("Status: ","200");
                    s=jsonObject.toString();
                    break;
                }
            }
            if(authorizeTag==0){
                jsonObject.put("Error: ","You need to log in.");
                jsonObject.put("Status: ","500");
                s=jsonObject.toString();
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
        @ResponseBody
        public String RegisterNewUser(@RequestHeader String username,@RequestHeader String password) throws JSONException {
            String result=" ";
            JSONObject jsonObject = new JSONObject();
            int code=0;
            //0 means OK
            //1 means occupied
            //2 means username is not a email address
            //3 means password is not strong enough

            //username=email? check
            if(isEmail(username)==false){
                jsonObject.put("Error message:","Username is not an email address.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }

            //password strong? check
            if(password.length()<=8){
                jsonObject.put("Error message:","Password length should be over 8.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }
            String reg = "[A-Z]";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(password);
            int j=0;
            while(matcher.find()){
                j++;
            }
            if(j==0){
                jsonObject.put("Error message:","You need at least one upper case char in password.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }

            String reg2= "[a-z]";
            Pattern pattern1 = Pattern.compile(reg2);
            Matcher matcher1 = pattern1.matcher(password);
            int k=0;
            while(matcher1.find()){
                k++;
            }
            if(k==0){
                jsonObject.put("Error message:","You need at least one lower case char in password.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }

            String reg3 = "[0-9]";
            Pattern pattern2 = Pattern.compile(reg3);
            Matcher matcher2 = pattern2.matcher(password);
            int l=0;
            while(matcher2.find()){
                l++;
            }
            if(l==0){
                jsonObject.put("Error message:","You need at least one number in password.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }




            List<User> a=userRepository.findAll();
            for(User singleRecord:a){
                if(singleRecord.getName().equals(username)){code=1;}
            }
            if(code==1){
                jsonObject.put("Error message:","Sorry, this account already exists!");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }
            if(code==0){
                User temp=new User();
                temp.setName(username);
                String codepass =  BCrypt.hashpw(password, BCrypt.gensalt());
                temp.setPassword(codepass);
                temp.setRealpassword(password);
                userRepository.save(temp);
                jsonObject.put("Message:","Register success!");
                jsonObject.put("Your username:",temp.getName());
                jsonObject.put("Your password:",temp.getRealpassword());
                jsonObject.put("Status:","200");
                result=jsonObject.toString();

            }

            return result;
        }





        //function of assignment3: CRUD
        @RequestMapping(value = "/Create",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String Create(@RequestHeader String username, @RequestHeader String password,
                             @RequestHeader String noteTitle,@RequestHeader String content) throws JSONException {
            List<User> a=userRepository.findAll();
            String currentUser=null;
            String result=null;
            int match=0;
            JSONObject jsonObject = new JSONObject();

            for(User singleRecord:a){
                if((singleRecord.getName().equals(username))&&BCrypt.checkpw(password,singleRecord.getpassword() )){
                    match=1;//match a legal user, go on
                    currentUser=singleRecord.getName();
                    break;
                }
            }
            //check if user-password is valid
            if(match==0){
                jsonObject.put("Error message:","Sorry, username and password are not valid!");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }
            match=1;

            if(noteTitle==null){
                jsonObject.put("User:",currentUser);
                jsonObject.put("Condition:","User log in success!");
                jsonObject.put("Error message:","Note title can't be null.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }

            if(content==null){
                jsonObject.put("User:",currentUser);
                jsonObject.put("Condition:","User log in success!");
                jsonObject.put("Error message:","Note content can't be null.");
                jsonObject.put("Status:","500");
                String p=jsonObject.toString();
                return p;
            }

            if(match==1){
                Note tempnote=new Note();
                tempnote.setOwner_name(currentUser);
                tempnote.setContent(content);
                tempnote.setNote_title(noteTitle);
                noteRepository.save(tempnote);
                jsonObject.put("User:",currentUser);
                jsonObject.put("Condition:","User log in success!");
                jsonObject.put("Operation:","Create a new note");
                jsonObject.put("Result:","Create success!");
                jsonObject.put("New note title:",tempnote.getNote_title());
                jsonObject.put("Status:","200");
                result=jsonObject.toString();

            }
            return result;
        }

}





