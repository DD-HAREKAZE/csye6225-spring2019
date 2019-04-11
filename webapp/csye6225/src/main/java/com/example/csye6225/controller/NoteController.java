package com.example.csye6225.controller;

import com.example.csye6225.entities.FilePath;
import com.example.csye6225.entities.Note;
import com.example.csye6225.dao.FilePathRepository;
import com.example.csye6225.dao.NoteRepository;
import com.example.csye6225.dao.UserRepository;
import com.example.csye6225.helpers.Helper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import java.util.List;

@RestController    // This means that this class is a Controller

public class NoteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private FilePathRepository filePathRepository;

    @Autowired
    private FilePathService filePathService;

    @Autowired
    private Helper helper;

    @RequestMapping(value = "/note", method = RequestMethod.GET, produces = "application/json")
    public String getAllNote(HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = helper.getUserID(header);

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
    public String getNote(HttpServletRequest request, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = helper.getUserID(header);

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
    public String postNote(@RequestBody Note note, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();

        if (note.getContent().length() < 4096) {
            String header = request.getHeader("Authorization");
            if (header != null) {

                int userID;
                userID = helper.getUserID(header);
                if (userID > -1) {

                    if (note.getTitle() != null && note.getContent() != null) {

                        note.setUserID(userID);
                        String hehe = new Date().toString();
                        note.setCreated_on(hehe);
                        note.setLast_updated_on(hehe);
                        noteRepository.save(note);

                        jsonObject.addProperty("id", note.getID());
                        jsonObject.addProperty("content", note.getContent());
                        jsonObject.addProperty("title", note.getTitle());
                        jsonObject.addProperty("created_on", note.getCreated_on());
                        jsonObject.addProperty("last_updated_on", note.getLast_updated_on());

                        JsonArray jsonArray = new JsonArray();
                        jsonObject.add("attachments", jsonArray);

                        response.setStatus(HttpServletResponse.SC_CREATED);
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
    public String updateNote(@RequestBody Note note, HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = helper.getUserID(header);

            if (userID > -1) {
                Optional<Note> t = noteRepository.findById(noteID);
                Note n = t.isPresent() ? t.get() : null;
                if (n != null) {
                    if (n.getUserID() == userID) {

                        if (note.getTitle() != null && note.getContent() != null) {

                            n.setTitle(note.getTitle());
                            n.setContent(note.getContent());
                            n.setLast_updated_on(new Date().toString());
                            noteRepository.save(n);

                            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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
    public String deleteNote(HttpServletRequest request, HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();
        String noteID = request.getRequestURI().split("/")[2];
        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = helper.getUserID(header);

            if (userID > -1) {
                Optional<Note> t = noteRepository.findById(noteID);
                Note note = t.isPresent() ? t.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {

                        List<FilePath> filePath = filePathRepository.findByNoteID(noteID);
                        if (!filePath.isEmpty()) {
                            for (FilePath f : filePath) {
                                filePathService.delete(f.getFilename());
                                filePathRepository.delete(f);
                            }
                        }
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

}
