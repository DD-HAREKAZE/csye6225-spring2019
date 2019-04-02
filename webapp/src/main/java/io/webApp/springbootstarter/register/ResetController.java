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

   @RequestMapping(method = RequestMethod.POST, value = "/reset")
	public String reserPassword(@RequestBody register userDetails) {
		statsd.incrementCounter(userHTTPPOST);

		logger.info("POST request : \"/reset\"");

		if (userDetails.getEmail() == null) {
			logger.error("Credentials should not be empty");
			return "{\"RESPONSE\" : \"User email not provided\"}";
		}

		logger.debug("Reset password for Email id : " + userDetails.getEmail());

		if (checkVaildEmailAddr(userDetails.getEmail())) {
			if (!checkAlreadyPresent(userDetails)) {
				logger.debug("This user is not registered with us");
				return "{\"RESPONSE\" : \"User not registered ! Please register\"}";
			}
		}

		AmazonSNS snsClient = AmazonSNSClient.builder().withRegion("us-east-1")
				.withCredentials(new InstanceProfileCredentialsProvider(false)).build();

		String resetEmail = userDetails.getEmail();
		logger.info("Reset Email: " + resetEmail);

		PublishRequest publishRequest = new PublishRequest(topicArn, userDetails.getEmail());
		PublishResult publishResult = snsClient.publish(publishRequest);
		logger.info("SNS Publish Result: " + publishResult);

		return "{\"RESPONSE\" : \"Password Reset Link was sent to your emailID\"}";
	}
}
