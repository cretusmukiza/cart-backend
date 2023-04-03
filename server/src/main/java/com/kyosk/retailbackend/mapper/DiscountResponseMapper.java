package com.kyosk.retailbackend.mapper;

import com.kyosk.retailbackend.DiscountResponse;
import com.kyosk.retailbackend.DiscountType;
import com.kyosk.retailbackend.entity.Discount;
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
