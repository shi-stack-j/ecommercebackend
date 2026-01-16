package com.shivam.aiecommercebackend.utility;

import com.shivam.aiecommercebackend.entity.UserDetailsPrincipal;
import com.shivam.aiecommercebackend.entity.UserEn;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthUtil {
    @Value("${jwt.secret-key}")
    private String secret_key;

//    To generate the secrete key
    public SecretKey generateKey(){
        return Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetailsPrincipal principal){
        String token= Jwts.builder()
                .subject(principal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 1000*60*60*24))
                .signWith(generateKey())
                .compact();
        return token;
    }

    public Claims getClaims(String token){
        Claims claims=Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    public String getUsername(String token){
        String username=getClaims(token)
                .getSubject();
        return username;
    }
    public boolean isTokenExpired(String token){
        boolean isExpired=getClaims(token).getExpiration().before(new Date());
        return isExpired;
    }

    public boolean validateToken(String token,UserDetailsPrincipal principal){
        boolean isValid=getClaims(token).getSubject().equals(principal.getUsername()) && !isTokenExpired(token);
        return isValid;
    }
}
