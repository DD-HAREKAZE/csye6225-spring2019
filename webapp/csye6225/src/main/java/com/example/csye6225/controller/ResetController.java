package com.example.csye6225.controller;

import com.example.csye6225.dao.UserRepository;
import com.example.csye6225.entities.User;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

@RestController
public class ResetController {


    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = "application/json")
    public String reset(@RequestBody String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();

        try{
            Optional<User> u = userRepository.findById(email);
            User user = u.isPresent() ? u.get() : null;
        }catch (Exception e){

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("Message", "user does not exist");
            jsonObject.addProperty("Code Status", response.getStatus());
            return jsonObject.toString();
        }
        String msg;
        String topicArn;
        try{
            AmazonSNSClient snsClient = new AmazonSNSClient();
            snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

            CreateTopicRequest createTopicRequest = new CreateTopicRequest("reset");
            CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
            topicArn = createTopicResult.getTopicArn();

            msg = email;
            PublishRequest publishRequest = new PublishRequest(topicArn, msg);
            PublishResult publishResult = snsClient.publish(publishRequest);

        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("Code Status", response.getStatus());
            jsonObject.addProperty("message", e.getMessage());
            return jsonObject.toString();
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        jsonObject.addProperty("Code Status", response.getStatus());
        jsonObject.addProperty("Status","topic pulish successfully!");
        jsonObject.addProperty("topic ARN", topicArn);
        jsonObject.addProperty("Request password user",msg);

        return jsonObject.toString();
    }
}
