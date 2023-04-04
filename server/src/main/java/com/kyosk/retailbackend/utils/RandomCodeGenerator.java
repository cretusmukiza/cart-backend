package com.kyosk.retailbackend.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomCodeGenerator {
    static final private String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    final private Random random = new SecureRandom();

    private char randomChar(){
        return ALPHABET.charAt(random.nextInt(ALPHABET.length()));
    }

    private String randomUUID(int length, int spacing, char spacerChar){
        StringBuilder sb = new StringBuilder();
        int spacer = 0;
        while(length > 0){
            if(spacer == spacing){
                sb.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            sb.append(randomChar());
        }
        return sb.toString();
    }
    public String generateProductCode(){
        return this.randomUUID(10, 2, '\u0000');
    }

    public String generateDiscountCode(){
        return this.randomUUID(10, 3, '-');
    }


}
