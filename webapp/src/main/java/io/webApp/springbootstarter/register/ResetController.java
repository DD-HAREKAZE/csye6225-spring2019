package io.webApp.springbootstarter.register;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${ARN}")
    private String topicArn;

    @RequestMapping(value = "/reset", method = RequestMethod.POST, produces = "application/json")
    public String reset(@RequestBody String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();

        register user = userRepository.findByEmail(email);

//        try{
//            register user = userRepository.findByEmail(email);
//
//        }catch (Exception e){
//
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            jsonObject.addProperty("Message", "user does not exist");
//            jsonObject.addProperty("Code Status", response.getStatus());
//            return jsonObject.toString();
//        }

        if(user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("Message", "user does not exist");
            jsonObject.addProperty("Code Status", response.getStatus());

        }else {
            try{
                AmazonSNSClient snsClient = new AmazonSNSClient();
                snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

                PublishRequest publishRequest = new PublishRequest(topicArn, email);
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
            jsonObject.addProperty("Request password user",email);
        }


        return jsonObject.toString();
    }
}
