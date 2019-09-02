package com.demo.mweb.products;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.mweb.ScheduledTasks;

@RestController
@RequestMapping(path = "/takealot")
public class TakelotController {
	
    private static final Logger log = LoggerFactory.getLogger(TakelotController.class);

    final String baseUrl = "https://api.takealot.com/rest/v-1-9-0/product-details/PLID";
    final String suffix = "?platform=desktop";
	
	@Autowired
	private RestTemplate restTemplate;

    @GetMapping("/{plid}")
    public ResponseEntity<String> get(@PathVariable("plid") String plid) {
    	String srs = baseUrl+plid+suffix;
    	try {
    		try {
    			HttpHeaders headers = new HttpHeaders();
    			headers.add("user-agent", "Application");
    			HttpEntity<String> entity = new HttpEntity<>(headers);
    			URI uri = new URI(baseUrl+plid+suffix);

    			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    			JSONObject json = new JSONObject(result.getBody());
    			JSONObject buybox = json.getJSONObject("buybox");
    			log.info(buybox.getString("pretty_price"));
    			Double price = buybox.getJSONArray("prices").getDouble(0);
    			String sprice =String.format("%.2f", price);
                return ResponseEntity.status(HttpStatus.OK).body(sprice);
    		} catch (URISyntaxException e) {
    			e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not retrieve price - 1 - "+ srs);
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not retrieve price - 2 - "+ srs);
    	}
    }

}
