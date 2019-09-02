package com.demo.mweb.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.mweb.User;

@RestController()
public class UserinfoController {

    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}