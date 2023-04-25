package com.cretus.retailbackend.mapper;

import com.cretus.retailbackend.OrderItemResponse;
import com.cretus.retailbackend.entity.OrderItem;
import com.cretus.retailbackend.utils.PriceUtil;
import com.cretus.retailbackend.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemResponseMapper {

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private DiscountResponseMapper discountResponseMapper;

    @Autowired
    private PriceUtil priceUtil;

    @Autowired
    private TimeUtils timeUtils;

    public OrderItemResponse mapResponse(OrderItem orderItem){
        OrderItemResponse.Builder builder = OrderItemResponse.newBuilder();
        builder.setId(orderItem.getId())
                .setProduct(productResponseMapper.mapResponse(orderItem.getProduct()));
        if(orderItem.getDiscount() != null){
            builder.setDiscount(this.discountResponseMapper.mapResponse(orderItem.getDiscount()));
        }
        builder.setPrice(this.priceUtil.toFloat(orderItem.getPrice()))
                .setQuantity(orderItem.getQuantity())
                .setTotalAmount(this.priceUtil.toFloat(orderItem.getTotalAmount()));
        if(orderItem.getDiscountAmount() != null){
            builder.setDiscountAmount(this.priceUtil.toFloat(orderItem.getDiscountAmount()));
        }
        builder.setFinalAmount(this.priceUtil.toFloat(orderItem.getFinalAmount()))
                .setCreatedAt(this.timeUtils.toGoogleTimestamp(orderItem.getCreatedAt()))
                .setUpdatedAt(this.timeUtils.toGoogleTimestamp(orderItem.getUpdatedAt()));

        return builder.build();

    }
}
