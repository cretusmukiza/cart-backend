package com.kyosk.retailbackend.service;

import com.kyosk.retailbackend.*;
import com.kyosk.retailbackend.constants.AuthConstant;
import com.kyosk.retailbackend.entity.*;
import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.interceptor.AuthInterceptor;
import com.kyosk.retailbackend.mapper.ShoppingCartItemResponseMapper;
import com.kyosk.retailbackend.repository.DiscountRepository;
import com.kyosk.retailbackend.repository.ProductRepository;
import com.kyosk.retailbackend.repository.ShoppingCartRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService(interceptors = {AuthInterceptor.class})
public class ShoppingCartService extends CartServiceGrpc.CartServiceImplBase {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartItemResponseMapper shoppingCartItemResponseMapper;

    @Override
    public void addItemToShoppingCart(AddItemToCartRequest request,
                                      StreamObserver<AddItemToCartResponse> responseObserver) {
        AddItemToCartResponse.Builder builder = AddItemToCartResponse.newBuilder();
        Optional<Product> optionalProduct = this.productRepository.findById(request.getProductId());

        // Check if an active shopping cart exists
        ShoppingCart shoppingCart;
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
        Optional<ShoppingCart> shoppingCartOptional = this.shoppingCartRepository.
                findByStatus(ShoppingCartStatus.ACTIVE);

        if(shoppingCartOptional.isPresent()){
            ShoppingCart existingShoppingCart = shoppingCartOptional.get();
            LocalDateTime expireTime = existingShoppingCart.getExpiresAt();

            // If the shopping is not modified after 24 hours mark it expired
            if(expireTime.isBefore(LocalDateTime.now())){
                existingShoppingCart.setStatus(ShoppingCartStatus.EXPIRED);
                this.shoppingCartRepository.save(existingShoppingCart);
                shoppingCart = new ShoppingCart();
                shoppingCart.setStatus(ShoppingCartStatus.ACTIVE);
                shoppingCart.setExpiresAt(expiresAt);
            }
            else {
                existingShoppingCart.setExpiresAt(expiresAt);
                shoppingCart = existingShoppingCart;
            }

        }
        // Else create a new shopping cart
        else {
            shoppingCart = new ShoppingCart();
            shoppingCart.setStatus(ShoppingCartStatus.ACTIVE);
            shoppingCart.setExpiresAt(expiresAt);
        }

        // Create shopping cart item
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(request.getQuantity());
            if(request.hasDiscountId()){
                Optional<Discount> optionalDiscount = this.discountRepository.findById(request.getDiscountId());
                if(optionalDiscount.isPresent()){
                    shoppingCartItem.setDiscount(optionalDiscount.get());
                }
            }

            List<ShoppingCartItem> items = new ArrayList<>();
            if(shoppingCart.getShoppingCartItems() != null){
                items.addAll(shoppingCart.getShoppingCartItems());
            }
            items.add(shoppingCartItem);
            shoppingCart.setShoppingCartItems(items);
            ShoppingCart savedShoppingCart = this.shoppingCartRepository.save(shoppingCart);
            List<ShoppingCartItem> savedItems = savedShoppingCart.getShoppingCartItems();
            ShoppingCartItem lastSavedItem = savedItems.get(savedItems.size() - 1);
            builder.setItem(this.shoppingCartItemResponseMapper.mapResponse(lastSavedItem));
            responseObserver.onNext(builder.build());
        }
        else {
            Status status = Status.NOT_FOUND.withDescription("The product is not found");
            responseObserver.onError(status.asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void cancelOrder(CancelOrderRequest request, StreamObserver<CancelOrderResponse> responseObserver) {
        CancelOrderResponse.Builder builder = CancelOrderResponse.newBuilder();
        User user = AuthConstant.AUTHORIZED_USER.get();
        builder.setSuccess(true);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
