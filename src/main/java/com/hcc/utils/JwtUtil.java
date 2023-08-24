package com.hcc.utils;

import com.hcc.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    //how long is the token valid? a whole day
    public static final long JWT_TOKEN_VALIDITY = 6000 * 60000 * 24;

    // get the jwt secret from the properties file
    @Value("${jwt.secret}")
    private String secret;

    //generate token
    public String generateToken(User user){
        return doGenerateToken(user.getUsername(), user.getAuthorities());
    }

    private String doGenerateToken(String subject, Collection<? extends GrantedAuthority> authorities){
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", authorities);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //validate token
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //check if token is expired
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }



    //get username from token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    //get the claims
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver ){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }




}

