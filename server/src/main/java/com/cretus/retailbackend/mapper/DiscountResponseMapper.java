package com.cretus.retailbackend.mapper;

import com.cretus.retailbackend.DiscountResponse;
import com.cretus.retailbackend.entity.Discount;
import org.springframework.stereotype.Component;

@Component
public class DiscountResponseMapper {
    public DiscountResponse mapResponse(Discount discount){
        DiscountResponse.Builder discountResponse = DiscountResponse.newBuilder();
        discountResponse.setId(discount.getId())
                .setCode(discount.getDiscountCode())
                .setDiscountType(discount.getDiscountType())
                .setDiscountValue(Float.valueOf(discount.getDiscountValue().toString()))
                .setIsActive(discount.isActive());
        return discountResponse.build();
    }
}
