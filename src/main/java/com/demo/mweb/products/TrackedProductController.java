package com.demo.mweb.products;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trackedProduct")
public class TrackedProductController {

    private TrackedProductRepository products;
    private static final Logger log = LoggerFactory.getLogger(TrackedProductController.class);

    public TrackedProductController(TrackedProductRepository products) {
        this.products = products;
    }

    @PutMapping(path= "", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> update(@RequestBody TrackedProduct product) {
    	log.info("Updating Product");
    	try {
    		//Obviously in the real world that be some validation and data duplication checks
    		this.products.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product has been updated");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not update Tracked Product");
    	}
    }

    @PostMapping(path= "", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> save(@RequestBody TrackedProduct product) {
    	log.info("Adding Product");
    	try {
    		//Obviously in the real world that be some validation and data duplication checks
    		this.products.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product will be tracked");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create Product Tracking");
    	}
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> get(@PathVariable("email") String email) {
    	try {
    		List<TrackedProduct> myProducts = this.products.findByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(myProducts);
    	}
    	catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create Product Tracking");
    	}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
        	// Again some checking would need to be here
        	this.products.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product was removed");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An Error occurred while removing the product");
        }
    }
}