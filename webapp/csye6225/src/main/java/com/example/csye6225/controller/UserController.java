package com.example.csye6225.controller;

import com.example.csye6225.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController

public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    // assignment1 GET method:/ return current time
    //redo: add basic auth
    @RequestMapping(value = "/",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getTime(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String s=" ";
        JSONObject jsonObject = new JSONObject();
        String header= request.getHeader("Authorization");
        String basicAuthEncoded = header.substring(6);
        String basicAuthAsString = new String(Base64.getDecoder().decode(basicAuthEncoded.getBytes()));
        final String[] credentialValues = basicAuthAsString.split(":", 2);

        String iusername=credentialValues[0];
        String ipassword=credentialValues[1];

        List<User> a=userRepository.findAll();

        int authorizeTag=0;//1 means authorized
        for (User singleRecord:a
        ) {
            //System.out.println("Now identifying "+singleRecord.getName()+","+singleRecord.getpassword());
            if((singleRecord.getName().equals(iusername))&&(singleRecord.getRealpassword().equals(ipassword)) ){
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
    @RequestMapping(value = "/user/register",method = RequestMethod.POST,produces="application/json")
    @ResponseBody
    public String RegisterNewUser(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        String result=" ";
        JSONObject jsonObject = new JSONObject();
        List<User> a=userRepository.findAll();
        int code=0;
        //0 means OK
        //1 means occupied
        //2 means username is not a email address
        //3 means password is not strong enough

        String header= request.getHeader("Authorization");
        String basicAuthEncoded = header.substring(6);
        String basicAuthAsString = new String(Base64.getDecoder().decode(basicAuthEncoded.getBytes()));
        final String[] credentialValues = basicAuthAsString.split(":", 2);

        String username=credentialValues[0];
        String password=credentialValues[1];


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

}
