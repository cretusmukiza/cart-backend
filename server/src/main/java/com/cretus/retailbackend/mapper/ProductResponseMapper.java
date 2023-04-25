package com.cretus.retailbackend.mapper;

import com.cretus.retailbackend.*;
import com.cretus.retailbackend.entity.Product;
import com.cretus.retailbackend.entity.ProductAttribute;
import com.cretus.retailbackend.entity.ProductInventory;
import com.cretus.retailbackend.entity.ProductPrice;
import com.cretus.retailbackend.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductResponseMapper {
    @Autowired
    private TimeUtils timeUtils;

    @Autowired
    private DiscountResponseMapper discountResponseMapper;

    public ProductResponse mapResponse(Product product){
        ProductResponse.Builder  builder = ProductResponse.newBuilder();

        // Build Product response
        builder.setId(product.getId())
                .setName(product.getName())
                .setProductCode(product.getProductCode())
                .setDescription(product.getDescription())
                .setImage(product.getImage())
                .setCreatedAt(timeUtils.toGoogleTimestamp(product.getCreatedAt()))
                .setUpdatedAt(timeUtils.toGoogleTimestamp(product.getUpdatedAt()));

        // Build product price response
        ProductPriceResponse.Builder productPriceBuilder = ProductPriceResponse.newBuilder();
        ProductPrice productPrice = product.getProductPrice();
        productPriceBuilder.setId(product.getId())
                .setPrice(Float.valueOf(productPrice.getPrice().toString()))
                .setCreatedAt(timeUtils.toGoogleTimestamp(productPrice.getCreatedAt()))
                .setUpdatedAt(timeUtils.toGoogleTimestamp(productPrice.getUpdatedAt()));

        builder.setPrice(productPriceBuilder.build());

        //

        // Build product inventory
        ProductInventoryResponse.Builder productInventoryResponse = ProductInventoryResponse.newBuilder();
        ProductInventory productInventory = product.getProductInventory();
        productInventoryResponse.setId(productInventory.getId())
                .setProductId(product.getId())
                .setQuantity(productInventory.getQuantity())
                .setReorderLevel(productInventory.getReorderLevel())
                .setCreatedAt(timeUtils.toGoogleTimestamp(productInventory.getCreatedAt()))
                .setUpdatedAt(timeUtils.toGoogleTimestamp(productInventory.getUpdatedAt()));

        builder.setProductInventory(productInventoryResponse.build());

        //Build product attributes
        List<ProductAttributesResponse> productAttributesResponse = product.getProductAttributeList().stream()
                .map((ProductAttribute productAttribute)-> {
                    ProductAttributesResponse.Builder productAttributesResponseItem = ProductAttributesResponse.newBuilder();
                    productAttributesResponseItem.setId(productAttribute.getId())
                            .setProductId(product.getId())
                            .setKey(productAttribute.getAttributeKey())
                            .setValue(productAttribute.getAttributeValue());
                    return productAttributesResponseItem.build();
                }).collect(Collectors.toList());
        builder.addAllProductAttributes(productAttributesResponse);

        // Build Discount response
        List<DiscountResponse> discountResponse = product.getDiscounts().stream()
                .map(discount-> this.discountResponseMapper.mapResponse(discount))
                .collect(Collectors.toList());
        builder.addAllDiscounts(discountResponse);

        return builder.build();
    }
}
