package com.kyosk.retailbackend.mapper;

import com.kyosk.retailbackend.ShoppingCartItemResponse;
import com.kyosk.retailbackend.ShoppingCartResponse;
import com.kyosk.retailbackend.ViewShoppingCartResponse;
import com.kyosk.retailbackend.entity.ShoppingCart;
import com.kyosk.retailbackend.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ShoppingCartResponseMapper {

    @Autowired
    private  ShoppingCartItemResponseMapper shoppingCartItemResponseMapper;

    @Autowired
    private TimeUtils timeUtils;

    public ShoppingCartResponse mapResponse(ShoppingCart shoppingCart){
        List<ShoppingCartItemResponse> shoppingCartItemResponses = shoppingCart.getShoppingCartItems().stream()
                .map(item -> this.shoppingCartItemResponseMapper.mapResponse(item)).collect(Collectors.toList());

        ShoppingCartResponse.Builder shoppingCartResponse = ShoppingCartResponse.newBuilder();
        shoppingCartResponse.setId(shoppingCart.getId())
                .addAllItems(shoppingCartItemResponses)
                .setStatus(shoppingCart.getStatus())
                .setExpiresAt(timeUtils.toGoogleTimestamp(shoppingCart.getExpiresAt()));
        return shoppingCartResponse.build();
    }
}
