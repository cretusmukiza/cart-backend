package com.kyosk.retailbackend.service;

import com.kyosk.retailbackend.*;
import com.kyosk.retailbackend.entity.*;
import com.kyosk.retailbackend.mapper.DiscountResponseMapper;
import com.kyosk.retailbackend.mapper.ProductResponseMapper;
import com.kyosk.retailbackend.repository.DiscountRepository;
import com.kyosk.retailbackend.repository.ProductRepository;
import com.kyosk.retailbackend.utils.RandomCodeGenerator;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class ProductService extends RetailServiceGrpc.RetailServiceImplBase {

    @Autowired
    private RandomCodeGenerator randomCodeGenerator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private DiscountResponseMapper discountResponseMapper;

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

    @Override
    public void getAllProducts(GetAllProductsRequest request, StreamObserver<GetAllProductsResponse> responseObserver) {
        GetAllProductsResponse.Builder builder = GetAllProductsResponse.newBuilder();
        List<ProductResponse>  productsResponse = this.productRepository.findAll().stream()
                .map(product -> this.productResponseMapper.mapResponse(product))
                .collect(Collectors.toList());
        builder.addAllProducts(productsResponse);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addProductQuantity(AddProductQuantityRequest request, StreamObserver<AddProductQuantityResponse> responseObserver) {
        AddProductQuantityResponse.Builder builder = AddProductQuantityResponse.newBuilder();
        Optional<Product> productOptional = this.productRepository.findById(request.getProductId());
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            ProductInventory productInventory = product.getProductInventory();
            productInventory.setQuantity((int) (productInventory.getQuantity() + request.getQuantity()));
            product.setProductInventory(productInventory);
            this.productRepository.save(product);
            builder.setSuccess(true);
        }
        else {
            Status status = Status.NOT_FOUND.withDescription("The product is not found");
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void changeProductPrice(ChangeProductPriceRequest request, StreamObserver<ChangeProductPriceResponse> responseObserver) {
        ChangeProductPriceResponse.Builder builder = ChangeProductPriceResponse.newBuilder();
        Optional<Product> productOptional = this.productRepository.findById(request.getProductId());
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            ProductPrice productPrice = product.getProductPrice();
            productPrice.setPrice(BigDecimal.valueOf(request.getNewPrice()));
            product.setProductPrice(productPrice);
            this.productRepository.save(product);
            builder.setSuccess(true);
        }
        else {
            Status status = Status.NOT_FOUND.withDescription("The product is not found");
            responseObserver.onError(status.asRuntimeException());
            return;
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<GetProductResponse> responseObserver) {
        GetProductResponse.Builder  builder = GetProductResponse.newBuilder();
        Optional<Product> productOptional = this.productRepository.findById(request.getProductId());
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            builder.setProduct(this.productResponseMapper.mapResponse(product));
        }
        else {
            Status status = Status.NOT_FOUND.withDescription("The product is not found");
            responseObserver.onError(status.asRuntimeException());
            return;
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createDiscount(CreateDiscountRequest request, StreamObserver<CreateDiscountResponse> responseObserver) {
        CreateDiscountResponse.Builder builder = CreateDiscountResponse.newBuilder();
        System.out.println(request.getDiscountType());
        Discount discount = new Discount();
        discount.setDiscountType(request.getDiscountType());
        discount.setDiscountCode(this.randomCodeGenerator.generateDiscountCode());
        discount.setDiscountValue(BigDecimal.valueOf(request.getDiscountValue()));
        discount.setActive(true);
        Discount savedDiscount = this.discountRepository.save(discount);
        builder.setDiscount(discountResponseMapper.mapResponse(savedDiscount));
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
