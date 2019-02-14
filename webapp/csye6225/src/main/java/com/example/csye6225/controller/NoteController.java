//This code explains us the note operations.
package com.example.csye6225.controller;

import com.example.csye6225.Note;
import com.example.csye6225.User;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

@Controller    // This means that this class is a Controller

public class NoteController {

    @Autowired
    // This means to get the bean called userRepository which is auto-generated by Spring, we will use it to handle the data
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/allnotes",method= RequestMethod.GET,produces="application/json")
    public @ResponseBody String getNotes (HttpServletRequest request, HttpServletResponse response){

        JsonObject jsonObject = new JsonObject();

        String header = request.getHeader("Authorization");
        if(header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {

                List<Note> note = noteRepository.findByUserID(userID);
                if(note != null) {
                    int i=1;
                    for(Note n:note) {
                        System.out.println(n.getID());
                        if(n.getUserID() == userID) {
                            System.out.println(userID);
                            JsonObject j = new JsonObject();
                            j.addProperty("ID",n.getID());
                            j.addProperty("title",n.getTitle());
                            j.addProperty("description",n.getDescription());
                            jsonObject.add(String.valueOf(i),j);
                            i++;
                        }
                        else{
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            jsonObject.addProperty("message", "You are not authorized to perform this activity");
                        }
                    }
                    jsonObject.addProperty("message", "note got successfully.");
                    jsonObject.addProperty("userID",userID);
                    return jsonObject.toString();

                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonObject.addProperty("message", "This is a bad request");
                }

            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "You are not authorized to perform this activity");
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "You are not authorized to perform this activity");
        }


        return jsonObject.toString();

    }

    @RequestMapping(value="/notes/{id}",method= RequestMethod.GET,produces="application/json")
    public @ResponseBody String getNote (HttpServletRequest request, HttpServletResponse response){

        JsonObject jsonObject = new JsonObject();

        int noteID = Integer.parseInt(request.getRequestURI().split("/")[2]);

        String header = request.getHeader("Authorization");
        if(header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {

                Note note = noteRepository.findById(noteID).get();
                if(note != null) {
                    if(note.getUserID() == userID) {
                        jsonObject.addProperty("message", "Note got successfully.");
                        jsonObject.addProperty("userID",note.getUserID());
                        jsonObject.addProperty("ID",note.getID());
                        jsonObject.addProperty("title",note.getTitle());
                        jsonObject.addProperty("description",note.getDescription());
                        return jsonObject.toString();
                    }
                    else{
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        jsonObject.addProperty("message", "You are not authorized to perform this activity");
                    }
                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonObject.addProperty("message", "This is a bad request");
                }

            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "You are not authorized to perform this activity");
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "You are not authorized to perform this activity");
        }


        return jsonObject.toString();

    }

    @RequestMapping(value = "/addnotes", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String addNote(@RequestHeader String title, @RequestHeader String description, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("start");
        JsonObject jsonObject = new JsonObject();

        if(description.length() < 4096) {
            String header = request.getHeader("Authorization");
            System.out.println("auth");
            if (header != null) {

                int userID;
                userID = GetUserDetails(header);
                if (userID > -1) {

                    Note note = new Note();
                    note.setUserID(userID);
                    note.setTitle(title);
                    note.setDescription(description);

                    noteRepository.save(note);


                    jsonObject.addProperty("message", "note has been created successfully for the User.");
                    jsonObject.addProperty("userId", note.getUserID());
                    jsonObject.addProperty("noteID", note.getID());
                    jsonObject.addProperty("title",note.getTitle());
                    jsonObject.addProperty("noteDescription", note.getDescription());
                    return jsonObject.toString();
                }
                else
                {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    jsonObject.addProperty("message", "You are not authorized to perform this activity");
                }
            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "You are not authorized to perform this activity");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("message", "Description exceeds maximum number of allowed characters.");
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="/notes/{id}",method= RequestMethod.PUT,produces="application/json")
    public @ResponseBody String updateNote (@RequestHeader String title, @RequestHeader String description, HttpServletRequest request, HttpServletResponse response){

        JsonObject jsonObject = new JsonObject();

        int noteID = Integer.parseInt(request.getRequestURI().split("/")[2]);

        String header = request.getHeader("Authorization");
        if(header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {

                Note note = noteRepository.findById(noteID).get();
                if(note != null) {
                    if(note.getUserID() == userID) {
                        note.setTitle(title);
                        note.setDescription(description);
                        noteRepository.save(note);
                        jsonObject.addProperty("message", "note updated successfully.");
                        return jsonObject.toString();
                    }
                    else{
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        jsonObject.addProperty("message", "You are not authorized to perform this activity");
                    }
                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonObject.addProperty("message", "This is a bad request");
                }

            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "You are not authorized to perform this activity");
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "You are not authorized to perform this activity");
        }


        return jsonObject.toString();

    }


    @RequestMapping(value="/notes/{id}",method= RequestMethod.DELETE,produces="application/json")
    public @ResponseBody String deleteNote (HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();
        int noteID = Integer.parseInt(request.getRequestURI().split("/")[2]);
        String header = request.getHeader("Authorization");
        if(header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {

                Note note = noteRepository.findById(noteID).get();
                if(note != null) {
                    if(note.getUserID() == userID) {
                        noteRepository.delete(note);
                        jsonObject.addProperty("message", "note deleted successfully.");
                        return jsonObject.toString();
                    }
                    else{
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        jsonObject.addProperty("message", "You are not authorized to perform this activity");
                    }

                }
                else{
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonObject.addProperty("message", "This is a bad request");
                }

            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "You are not authorized to perform this activity");
            }
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "You are not authorized to perform this activity");
        }

        return jsonObject.toString();

    }

    public int GetUserDetails(String header) {
        //This is the logic to fetch user password from the authorization header value by removing "Basic" keyword, decoding and splitting with :

        if (header != null && header.contains("Basic")) {
            String basicAuthEncoded = header.substring(6);
            String basicAuthAsString = new String(Base64.getDecoder().decode(basicAuthEncoded.getBytes()));


            final String[] credentialValues = basicAuthAsString.split(":", 2);
            //If user exists in DB , return the user object.
            User user = validateUser(credentialValues[0], credentialValues[1]);
            if(user != null) {
                return user.getID();
            }
        }

        return -1;


    }


    public User validateUser(String username, String password)
    {
        for(User user:userRepository.findAll())
        {
            if((user.getName().equals(username))&&BCrypt.checkpw(password,user.getpassword() ) )
                return user;

        }
        return null;
    }


}
