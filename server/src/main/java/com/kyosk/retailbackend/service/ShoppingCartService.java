package com.kyosk.retailbackend.service;

import com.kyosk.retailbackend.*;
import com.kyosk.retailbackend.constants.AuthConstant;
import com.kyosk.retailbackend.entity.*;
import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.interceptor.AuthInterceptor;
import com.kyosk.retailbackend.mapper.ShoppingCartItemResponseMapper;
import com.kyosk.retailbackend.mapper.ShoppingCartResponseMapper;
import com.kyosk.retailbackend.repository.DiscountRepository;
import com.kyosk.retailbackend.repository.ProductRepository;
import com.kyosk.retailbackend.repository.ShoppingCartRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    private ShoppingCartResponseMapper shoppingCartResponseMapper;

    @Override
    public void addItemToShoppingCart(AddItemToCartRequest request,
                                      StreamObserver<AddItemToCartResponse> responseObserver) {
        AddItemToCartResponse.Builder builder = AddItemToCartResponse.newBuilder();
        Optional<Product> optionalProduct = this.productRepository.findById(request.getProductId());
        if(optionalProduct.isPresent()){
            // Get user active shopping cart
            User user = AuthConstant.AUTHORIZED_USER.get();
            List<ShoppingCart> shoppingCartList = user.getShoppingCartList();
            Optional<ShoppingCart> activeShoppingCart = shoppingCartList.stream()
                    .filter(item -> item.getStatus().equals(ShoppingCartStatus.ACTIVE)).findFirst();

            // Check if an active shopping cart exists
            ShoppingCart shoppingCart;
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

            if(activeShoppingCart.isPresent()){
                ShoppingCart existingShoppingCart = activeShoppingCart.get();
                LocalDateTime expireTime = existingShoppingCart.getExpiresAt();

                // If the shopping is not modified after 24 hours mark it expired
                if(expireTime.isBefore(LocalDateTime.now())){
                    existingShoppingCart.setStatus(ShoppingCartStatus.EXPIRED);
                    this.shoppingCartRepository.save(existingShoppingCart);
                    shoppingCart = new ShoppingCart();
                    shoppingCart.setStatus(ShoppingCartStatus.ACTIVE);
                    shoppingCart.setExpiresAt(expiresAt);
                    shoppingCart.setUser(user);
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
                shoppingCart.setUser(user);
            }

            // Create shopping cart item
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            Product product = optionalProduct.get();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(request.getQuantity());
            if(request.hasDiscountId()){
                Optional<Discount> optionalDiscount = this.discountRepository.findById(request.getDiscountId());
                if(optionalDiscount.isPresent()){
                    shoppingCartItem.setDiscount(optionalDiscount.get());
                }
            }

            // Add item to shopping cart

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
    public void viewShoppingCart(ViewShoppingCartRequest request,
                                 StreamObserver<ViewShoppingCartResponse> responseObserver) {
        ViewShoppingCartResponse.Builder builder = ViewShoppingCartResponse.newBuilder();
        User user = AuthConstant.AUTHORIZED_USER.get();
        List<ShoppingCart> shoppingCartList = user.getShoppingCartList();
        Optional<ShoppingCart> activeShoppingCart = shoppingCartList.stream()
                .filter(item -> item.getStatus().equals(ShoppingCartStatus.ACTIVE)).findFirst();
        if(activeShoppingCart.isPresent()){
            builder.setShoppingCart(this.shoppingCartResponseMapper.mapResponse(activeShoppingCart.get()));
            responseObserver.onNext(builder.build());
        }
        else{
            Status status = Status.NOT_FOUND.withDescription("The cart is not found, add new items to the cart");
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
