package com.kyosk.retailbackend.utils;

import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.entity.UserSession;
import com.kyosk.retailbackend.repository.UserRepository;
import com.kyosk.retailbackend.repository.UserSessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class JwtUtil {

    @Value("${app.jwt.expiration_in_hours}")
    private int  TOKEN_EXPIRATION_IN_HOURS;

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeUtils timeUtils;

    public String generateAccessToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime expiresAt = localDateTime.plusHours(TOKEN_EXPIRATION_IN_HOURS);

        String token =  Jwts.builder()
                .setSubject("User")
                .setClaims(claims)
                .setIssuedAt(this.timeUtils.localTimeToDate(localDateTime))
                .setExpiration(this.timeUtils.localTimeToDate(expiresAt))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        this.userSessionRepository.findByUser(user).ifPresent((userSession -> {
            this.userSessionRepository.delete(userSession);
        }));
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setToken(token);
        userSession.setExpiresAt(expiresAt);

        this.userSessionRepository.save(userSession);

        return token;
    }

    public User getUserFromToken(String token) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(this.SECRET_KEY)
                .parseClaimsJws(token).getBody();
        Long userId = claims.get("userId", Long.class);
        if(Objects.isNull(userId)){
            throw new Exception("Token is invalid");
        }
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new Exception("User is not found");
        }
        return optionalUser.get();
    }
}
