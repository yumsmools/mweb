package com.demo.mweb.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.demo.mweb.ScheduledTasks;
import com.demo.mweb.User;
import com.demo.mweb.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    private UserRepository userDetailsService;

    public String createToken(String username) {

        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
            .setClaims(claims)//
            .setIssuedAt(now)//
            .setExpiration(validity)//
            .signWith(secretKey)//
            .compact();
    }

    public Authentication getAuthentication(String token) {
    	log.info("my token is " + token);
        User userDetails = this.userDetailsService.findByEmail(getEmail(token));
        log.info("User is : " + userDetails.getEmail());
        return new UsernamePasswordAuthenticationToken(userDetails, "");
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        log.warn("Token is null");
        return null;
    }

    public boolean validateToken(String token) {
        try {
        	log.info("validate token");
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        	log.info("validate token");

            if (claims.getBody().getExpiration().before(new Date())) {
            	log.info("expired token");
                return false;
            }
        	log.info("valid token");

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Expired or invalid JWT token");
            return false;
        }
    }

}
