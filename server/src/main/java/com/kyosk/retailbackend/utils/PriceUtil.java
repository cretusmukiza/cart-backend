package com.kyosk.retailbackend.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceUtil {

    public Float toFloat(BigDecimal price){
        return Float.valueOf(price.toString());
    }

}
