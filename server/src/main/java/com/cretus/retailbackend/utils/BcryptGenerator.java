package com.cretus.retailbackend.utils;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BcryptGenerator {
    private BCryptPasswordEncoder  bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String encodePassword(String password){


        return this.bCryptPasswordEncoder.encode(password);
    }

    public boolean isValidPassword(String password, String hashedPassword){
        return bCryptPasswordEncoder.matches(password,hashedPassword);
    }

}
