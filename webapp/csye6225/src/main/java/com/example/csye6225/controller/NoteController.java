package com.example.csye6225.controller;

import com.example.csye6225.FilePath;
import com.example.csye6225.Note;
import com.example.csye6225.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Base64;
import java.util.List;

@Controller    // This means that this class is a Controller

public class NoteController {
    @Autowired
    private FilePathService filePathService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilePathRepository filePathRepository;

    @RequestMapping(value = "/note", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getAllNote(HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {

                List<Note> note = noteRepository.findByUserID(userID);

                if (!note.isEmpty()) {

                    JsonArray jsonArray = new JsonArray();
                    for (Note n : note) {

                        JsonObject j = new JsonObject();
                        j.addProperty("id", n.getID());
                        j.addProperty("content", n.getContent());
                        j.addProperty("title", n.getTitle());
                        j.addProperty("created_on", n.getCreated_on());
                        j.addProperty("last_updated_on", n.getLast_updated_on());

                        List<FilePath> filePath = filePathRepository.findByNoteID(n.getID());

                        if (!filePath.isEmpty()) {
                            JsonArray ja = new JsonArray();
                            for (FilePath f : filePath) {
                                JsonObject jj = new JsonObject();
                                jj.addProperty("id", f.getID());
                                jj.addProperty("url", f.getPath());
                                ja.add(jj);
                            }
                            j.add("attachments", ja);

                        } else {
                            j.addProperty("attachments", "");
                        }

                        jsonArray.add(j);

                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonArray.toString();

                } else {

                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonObject.addProperty("message", "404:Not Found");

                }

            } else {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "401:Unauthorized");

            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "401:Unauthorized");

        }


        return jsonObject.toString();

    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getNote(HttpServletRequest request, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {

                Optional<Note> o = noteRepository.findById(noteID);
                Note note = o.isPresent() ? o.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {

                        jsonObject.addProperty("id", note.getID());
                        jsonObject.addProperty("content", note.getContent());
                        jsonObject.addProperty("title", note.getTitle());
                        jsonObject.addProperty("created_on", note.getCreated_on());
                        jsonObject.addProperty("last_updated_on", note.getLast_updated_on());

                        List<FilePath> filePath = filePathRepository.findByNoteID(noteID);
                        if (!filePath.isEmpty()) {
                            JsonArray jsonArray = new JsonArray();
                            for (FilePath f : filePath) {
                                JsonObject j = new JsonObject();
                                j.addProperty("id", f.getID());
                                j.addProperty("url", f.getPath());
                                jsonArray.add(j);
                            }
                            jsonObject.add("attachments", jsonArray);
                        } else {
                            jsonObject.addProperty("attachments", "");
                        }
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject.toString();
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        jsonObject.addProperty("message", "401:Unauthorized");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonObject.addProperty("message", "404:Not Found");

                }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "401:Unauthorized");

            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "401:Unauthorized");

        }


        return jsonObject.toString();

    }

    @RequestMapping(value = "/note", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    String postNote(@RequestParam("file") MultipartFile file, @RequestParam String title, @RequestParam String content, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();

        if (content.length() < 4096) {
            String header = request.getHeader("Authorization");
            if (header != null) {

                int userID;
                userID = GetUserDetails(header);
                if (userID > -1) {

                    if (title != null && content != null) {

                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String hehe = dateFormat.format(now);
                        Note note = new Note();
                        note.setUserID(userID);
                        note.setContent(content);
                        note.setTitle(title);
                        note.setCreated_on(hehe);
                        note.setLast_updated_on(hehe);
                        noteRepository.save(note);

                        FilePath filePath = new FilePath();
                        filePath.setNoteID(note.getID());
                        filePath.setPath(filePathService.Upload(file));
                        filePath.setFilename(file.getOriginalFilename());
                        filePathRepository.save(filePath);

                        jsonObject.addProperty("id", note.getID());
                        jsonObject.addProperty("content", note.getContent());
                        jsonObject.addProperty("title", note.getTitle());
                        jsonObject.addProperty("created_on", note.getCreated_on());
                        jsonObject.addProperty("last_updated_on", note.getLast_updated_on());
                        JsonArray jsonArray = new JsonArray();
                        JsonObject j = new JsonObject();
                        j.addProperty("id", filePath.getID());
                        j.addProperty("url", filePath.getPath());
                        response.setStatus(HttpServletResponse.SC_CREATED);
                        jsonArray.add(j);
                        jsonObject.add("attachments",jsonArray);
                        return jsonObject.toString();

                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        jsonObject.addProperty("message", "400:Bad Request");
                    }

                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    jsonObject.addProperty("message", "401:Unauthorized");

                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "401:Unauthorized");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("message", "400:Bad Request");

        }

        return jsonObject.toString();
    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    String updateNote(@RequestParam("file") MultipartFile file, @RequestParam String title, @RequestParam String content, HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {
                Optional<Note> t = noteRepository.findById(noteID);
                Note note = t.isPresent() ? t.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {
                        if (title != null) note.setTitle(title);
                        if (content!= null) note.setContent(content);
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String hehe = dateFormat.format(now);
                        note.setLast_updated_on(hehe);
                        noteRepository.save(note);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);


                        return jsonObject.toString();
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        jsonObject.addProperty("message", "401:Unauthorized");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonObject.addProperty("message", "404:Not Found");
                }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "401:Unauthorized");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "401:Unauthorized");
        }


        return jsonObject.toString();

    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    String deleteNote(HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();
        String noteID = request.getRequestURI().split("/")[2];
        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {
                Optional<Note> t = noteRepository.findById(noteID);
                Note note = t.isPresent() ? t.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {
                        noteRepository.delete(note);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        return jsonObject.toString();
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        jsonObject.addProperty("message", "401:Unauthorized");
                    }

                } else {
                    System.out.println("notfound");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonObject.addProperty("message", "404:Not Found");
                }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonObject.addProperty("message", "401:Unauthorized");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonObject.addProperty("message", "401:Unauthorized");
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
