package com.kyosk.retailbackend.service;

import com.kyosk.retailbackend.*;
import com.kyosk.retailbackend.entity.Product;
import com.kyosk.retailbackend.entity.ProductAttribute;
import com.kyosk.retailbackend.entity.ProductInventory;
import com.kyosk.retailbackend.entity.ProductPrice;
import com.kyosk.retailbackend.mapper.ProductResponseMapper;
import com.kyosk.retailbackend.repository.ProductRepository;
import com.kyosk.retailbackend.utils.RandomCodeGenerator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@GrpcService
public class ProductService extends RetailServiceGrpc.RetailServiceImplBase {

    @Autowired
    private RandomCodeGenerator randomCodeGenerator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<CreateProductResponse> responseObserver) {
        CreateProductResponse.Builder builder = CreateProductResponse.newBuilder();

        // Creating product
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setProductCode(this.randomCodeGenerator.generateProductCode());
        product.setImage(request.getImage());

        // Creating product prices
        ProductPrice productPrice = new ProductPrice();
        productPrice.setPrice(BigDecimal.valueOf(request.getPrice()));
        productPrice.setProduct(product);
        product.setProductPrice(productPrice);

        // Creating product inventory
        ProductInventory productInventory = new ProductInventory();
        productInventory.setQuantity((int)request.getQuantity());
        productInventory.setReorderLevel((int)request.getReorderLevel());
        productInventory.setProduct(product);
        product.setProductInventory(productInventory);

        // Creating product attributes
        List<ProductAttribute> productAttributes = new ArrayList<>();
        for (ProductAttributesFields productAttributesFields: request.getProductAttributesList()){
            ProductAttribute productAttribute = new ProductAttribute();
            productAttribute.setAttributeKey(productAttributesFields.getKey());
            productAttribute.setAttributeValue(productAttributesFields.getValue());
            productAttributes.add(productAttribute);
        }
        product.setProductAttributeList(productAttributes);

        // Save product
        Product productSaved = this.productRepository.save(product);

        builder.setProduct(productResponseMapper.mapResponse(productSaved));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
