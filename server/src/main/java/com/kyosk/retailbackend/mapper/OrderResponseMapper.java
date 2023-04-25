package com.kyosk.retailbackend.mapper;

import com.kyosk.retailbackend.OrderResponse;
import com.kyosk.retailbackend.entity.Order;
import com.kyosk.retailbackend.utils.PriceUtil;
import com.kyosk.retailbackend.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderResponseMapper {

    @Autowired
    private PriceUtil priceUtil;

    @Autowired
    private OrderItemResponseMapper orderItemResponseMapper;

    @Autowired
    private TimeUtils timeUtils;

    public OrderResponse mapResponse(Order order){
        OrderResponse.Builder builder = OrderResponse.newBuilder();
        builder.setId(order.getId())
                .setOrderStatus(order.getStatus())
                .setGrandTotal(this.priceUtil.toFloat(order.getGrandTotal()))
                .setDiscountAmount(this.priceUtil.toFloat(order.getDiscountAmount()))
                .setFinalAmount(this.priceUtil.toFloat(order.getFinalAmount()))
                .addAllOrderItems(
                        order.getOrderItemList()
                                .stream()
                                .map(item -> this.orderItemResponseMapper
                                        .mapResponse(item)).collect(Collectors.toList()))
                .setCreatedAt(this.timeUtils.toGoogleTimestamp(order.getCreatedAt()));
        return builder.build();
    }
}
