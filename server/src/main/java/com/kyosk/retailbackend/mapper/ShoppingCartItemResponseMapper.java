package com.kyosk.retailbackend.mapper;


import com.kyosk.retailbackend.ShoppingCartItemResponse;
import com.kyosk.retailbackend.entity.ShoppingCartItem;
import com.kyosk.retailbackend.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartItemResponseMapper {

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private DiscountResponseMapper discountResponseMapper;

    @Autowired
    private TimeUtils timeUtils;

    public ShoppingCartItemResponse mapResponse(ShoppingCartItem shoppingCartItem){
        ShoppingCartItemResponse.Builder shoppingCartItemResponse = ShoppingCartItemResponse.newBuilder();
        if(shoppingCartItem.getId() != null){
            shoppingCartItemResponse.setId(shoppingCartItem.getId());
        }
        shoppingCartItemResponse.setProduct(this.productResponseMapper.mapResponse(shoppingCartItem.getProduct()))
                .setQuantity(shoppingCartItem.getQuantity())
                .setDiscount(discountResponseMapper.mapResponse(shoppingCartItem.getDiscount()))
                .setCreatedAt(timeUtils.toGoogleTimestamp(shoppingCartItem.getCreatedAt()))
                .setUpdatedAt(timeUtils.toGoogleTimestamp(shoppingCartItem.getUpdatedAt()));
        return shoppingCartItemResponse.build();
    }
}
