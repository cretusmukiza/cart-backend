package com.cretus.retailbackend.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PriceUtil {

    public Float toFloat(BigDecimal price){
        return Float.valueOf(price.toString());
    }

    public BigDecimal sanitizePrice(BigDecimal price) {
        return price.setScale(2, RoundingMode.CEILING);
    }
}
