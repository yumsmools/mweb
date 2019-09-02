package com.demo.mweb;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/login")
public class LoginController {
	
//	@Autowired
//	private RestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepo;
	
//	@Autowired
//	private JwtTokenProvider tokenProvider;

	ObjectMapper mapper = new ObjectMapper();
//    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> login(@RequestBody User user) {
    	Iterable<User> iterable = userRepo.findAll();
    	List<User> users = 
    			  StreamSupport.stream(iterable.spliterator(), false)
    			    .collect(Collectors.toList());
    	
    	try {
    		User fetchedUser = users.stream()
	    			.filter(d -> d.getEmail().equals(user.getEmail()))
	    			.collect(toSingleton());

        	if (fetchedUser.getPassword().equals(user.getPassword())) {
//        		String authenticationToken = tokenProvider.createToken(user.getEmail());
//        		log.info(authenticationToken);
//                return ResponseEntity.status(HttpStatus.OK).body(authenticationToken);
                return ResponseEntity.status(HttpStatus.OK).body("User is logged in");
        	}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Credentials provided - NF");
    	}
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials provided");
    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
