package com.demo.mweb;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.demo.mweb.products.TrackedProduct;
import com.demo.mweb.products.TrackedProductRepository;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired 
    private TrackedProductRepository productRepo;
	
	@Autowired
	private RestTemplate restTemplate;

    private static final String baseUrl = "https://api.takealot.com/rest/v-1-9-0/product-details/PLID";
    private static final String suffix = "?platform=desktop";

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent", "Application");
		HttpEntity<String> entity = new HttpEntity<>(headers);

    	Iterable<TrackedProduct> iterable = productRepo.findAll();
    	Iterator<TrackedProduct> iterator = iterable.iterator();
    	while(iterator.hasNext()) {
    		try {
	    		TrackedProduct product = iterator.next();
				URI uri = new URI(baseUrl+product.getPlid()+suffix);
	log.info("uri: " + uri);
				ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
				JSONObject json = new JSONObject(result.getBody());
				JSONObject buybox = json.getJSONObject("buybox");
				int price = buybox.getJSONArray("prices").getInt(0);
				
				product.setPrice(price);
				if (this.updatePrice(product, price) == false) {
					log.error("Could not update price for product " + product.getPlid());
				}
    		} catch (URISyntaxException e) {
    			e.printStackTrace();
    			log.error("Could Not update prices");
    		}
    	}
        log.info("Completed Updating Prices {}", dateFormat.format(new Date()));
    }
    
    public Boolean updatePrice(TrackedProduct product, int price) {
    	final String url = "http://localhost:8080/trackedProduct";
        
        HttpEntity<TrackedProduct> requestEntity = new HttpEntity<>(product);
        
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class );
        if (HttpStatus.OK.equals(response.getStatusCode())) {
        	return true;
        }
        return false;
    }
}