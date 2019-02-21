package com.example.csye6225.controller;

import com.example.csye6225.FilePath;
import com.example.csye6225.Note;
import com.example.csye6225.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class FileController {
    @Autowired
    private FilePathService filePathService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilePathRepository filePathRepository;


    //GET
    @RequestMapping(value = "/note/{idNotes}/attachments", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getAttachments(HttpServletRequest request, HttpServletResponse response) {

        JsonArray jsonArray = new JsonArray();
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
                        List<FilePath> filePath = filePathRepository.findByNoteID(noteID);
                        if(!filePath.isEmpty()) {
                            for (FilePath f : filePath) {

                                JsonObject j = new JsonObject();
                                j.addProperty("id", f.getID());
                                j.addProperty("url", f.getPath());
                                jsonArray.add(j);

                            }
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonArray.toString();
                        }
                        else {
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
    @ResponseBody
    public String postAttachments(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

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

                        FilePath filePath = new FilePath();
                        filePath.setNoteID(noteID);
                        filePath.setFilename(file.getOriginalFilename());
                        filePath.setPath(filePathService.Upload(file));
                        filePathRepository.save(filePath);

                        jsonObject.addProperty("id", filePath.getID());
                        jsonObject.addProperty("url", filePath.getPath());
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

    //PUT
    @RequestMapping(value = "/note/{idNotes}/attachments/{idAttachments}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String updateAttachments(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];
        String fileID = request.getRequestURI().split("/")[4];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {
                Optional<Note> on = noteRepository.findById(noteID);
                Note note = on.isPresent() ? on.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {
                        Optional<FilePath> of =filePathRepository.findById(fileID);
                        FilePath filePath= of.isPresent() ? of.get() : null;
                        if(filePath!=null) {
                            if(filePath.getNoteID().equals(noteID)) {

                                System.out.println(filePath.getFilename());
                                filePathService.delete(filePath.getFilename());
                                filePathRepository.delete(filePath);

                                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                                return jsonObject.toString();

                            }else {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                jsonObject.addProperty("message", "401:Unauthorized");
                            }
                        }else {
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
    @ResponseBody
    public String deleteAttachments(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject jsonObject = new JsonObject();

        String noteID = request.getRequestURI().split("/")[2];
        String fileID = request.getRequestURI().split("/")[4];

        String header = request.getHeader("Authorization");
        if (header != null) {

            int userID = GetUserDetails(header);

            if (userID > -1) {
                Optional<Note> on = noteRepository.findById(noteID);
                Note note = on.isPresent() ? on.get() : null;
                if (note != null) {
                    if (note.getUserID() == userID) {
                        Optional<FilePath> of =filePathRepository.findById(fileID);
                        FilePath filePath= of.isPresent() ? of.get() : null;
                        if(filePath!=null) {
                            if(filePath.getNoteID().equals(noteID)) {

                                System.out.println(filePath.getFilename());
                                filePathService.delete(filePath.getFilename());
                                filePathRepository.delete(filePath);

                                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                                return jsonObject.toString();

                            }else {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                jsonObject.addProperty("message", "401:Unauthorized");
                            }
                        }else {
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