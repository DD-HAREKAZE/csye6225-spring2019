
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class FileController {

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

    //GET
    @RequestMapping(value = "/note/{idNotes}/attachments", method = RequestMethod.GET, produces = "application/json")
    public String getAttachments(HttpServletRequest request, HttpServletResponse response) {

        JsonArray jsonArray = new JsonArray();
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
                        List<FilePath> filePath = filePathRepository.findByNoteID(noteID);
                        if (!filePath.isEmpty()) {
                            for (FilePath f : filePath) {

                                JsonObject j = new JsonObject();
                                j.addProperty("id", f.getID());
                                j.addProperty("url", f.getPath());
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

    //POST
    @RequestMapping(value = "/note/{idNotes}/attachments", method = RequestMethod.POST, produces = "application/json")
    public String postAttachments(@RequestParam MultipartFile[] file, HttpServletRequest request, HttpServletResponse response) throws IOException {

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

                        JsonArray jsonArray = new JsonArray();
                        for (MultipartFile f : file) {
                            FilePath filePath = new FilePath();
                            filePath.setNoteID(noteID);
                            filePath.setFilename(f.getOriginalFilename());
                            filePath.setID(UUID.randomUUID().toString());
                            filePath.setPath(filePathService.Upload(f,filePath.getID()));
                            filePathRepository.save(filePath);

                            JsonObject j = new JsonObject();
                            j.addProperty("id", filePath.getID());
                            j.addProperty("url", filePath.getPath());
                            jsonArray.add(j);
                        }

                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonArray.toString();
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

    //PUT
    @RequestMapping(value = "/note/{idNotes}/attachments/{idAttachments}", method = RequestMethod.PUT, produces = "application/json")
    public String updateAttachments(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];
        String fileID = request.getRequestURI().split("/")[4];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = helper.getUserID(header);

            if (userID > -1) {
                Optional<Note> on = noteRepository.findById(noteID);
                Note note = on.isPresent() ? on.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {

                        Optional<FilePath> of = filePathRepository.findById(fileID);
                        FilePath filePath = of.isPresent() ? of.get() : null;
                        if (filePath != null) {
                            if (filePath.getNoteID().equals(noteID)) {

                                filePath.setFilename(file.getOriginalFilename());
                                filePath.setPath(filePathService.Upload(file,filePath.getID()));
                                filePathRepository.save(filePath);

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

    //DELETE
    @RequestMapping(value = "/note/{idNotes}/attachments/{idAttachments}", method = RequestMethod.DELETE, produces = "application/json")
    public String deleteAttachments(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];
        String fileID = request.getRequestURI().split("/")[4];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = helper.getUserID(header);

            if (userID > -1) {
                Optional<Note> on = noteRepository.findById(noteID);
                Note note = on.isPresent() ? on.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {
                        Optional<FilePath> of = filePathRepository.findById(fileID);
                        FilePath filePath = of.isPresent() ? of.get() : null;
                        if (filePath != null) {
                            if (filePath.getNoteID().equals(noteID)) {

                                filePathService.delete(filePath.getID());
                                filePathRepository.delete(filePath);

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