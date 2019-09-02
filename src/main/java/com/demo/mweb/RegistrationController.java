package com.demo.mweb;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/register")
public class RegistrationController {
		
	@Autowired
	private RestTemplate restTemplate;
	
    @PostMapping(path= "", consumes = "application/json", produces = "text/plain")
    public String registerUser(@RequestBody User user)
    {
        final String baseUrl = "http://localhost:8080/user";
		try {
			URI uri = new URI(baseUrl);
			ResponseEntity<User> result = restTemplate.postForEntity(uri, user, User.class);
			return("User was created");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			return("Could not create User");
		}
	}
}
