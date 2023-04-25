package com.kyosk.retailbackend.service;

import com.kyosk.retailbackend.*;
import com.kyosk.retailbackend.constants.AuthConstant;
import com.kyosk.retailbackend.entity.*;
import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.interceptor.AuthInterceptor;
import com.kyosk.retailbackend.mapper.OrderResponseMapper;
import com.kyosk.retailbackend.mapper.ShoppingCartItemResponseMapper;
import com.kyosk.retailbackend.mapper.ShoppingCartResponseMapper;
import com.kyosk.retailbackend.repository.*;
import com.kyosk.retailbackend.utils.PriceUtil;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderResponseMapper orderResponseMapper;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  CancelledOrderRepository cancelledOrderRepository;

    @Autowired
    private PriceUtil priceUtil;

    @Override
    public void addItemToShoppingCart(AddItemToCartRequest request,
                                      StreamObserver<AddItemToCartResponse> responseObserver) {
        AddItemToCartResponse.Builder builder = AddItemToCartResponse.newBuilder();
        Optional<Product> optionalProduct = this.productRepository.findById(request.getProductId());
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            // Check if the quantity is available
            if(product.getProductInventory().getQuantity() < request.getQuantity()){
                Status status = Status.NOT_FOUND.withDescription("The requested quantity is not available");
                responseObserver.onError(status.asRuntimeException());
            }

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
    public void modifyCartItemQuantities(ModifyCartItemQuantitiesRequest request, StreamObserver<ModifyCartItemQuantitiesResponse> responseObserver) {
        ModifyCartItemQuantitiesResponse.Builder builder = ModifyCartItemQuantitiesResponse.newBuilder();
        Optional<ShoppingCartItem> shoppingCartItemOptional = this.shoppingCartItemRepository
                .findById(request.getCartItemId());
        if(shoppingCartItemOptional.isPresent()){
            ShoppingCartItem shoppingCartItem = shoppingCartItemOptional.get();
            Product product = shoppingCartItem.getProduct();
            if(product.getProductInventory().getQuantity() < request.getQuantity()){
                Status status = Status.NOT_FOUND.withDescription("The requested quantity is not available");
                responseObserver.onError(status.asRuntimeException());
            }
            shoppingCartItem.setQuantity(request.getQuantity());
            ShoppingCartItem savedShoppingCartItem = this.shoppingCartItemRepository.save(shoppingCartItem);
            builder.setItem(this.shoppingCartItemResponseMapper.mapResponse(savedShoppingCartItem));
            responseObserver.onNext(builder.build());

        }
        else{
            Status status = Status.NOT_FOUND.withDescription("The shopping cart item is not found");
            responseObserver.onError(status.asRuntimeException());
        }
        responseObserver.onCompleted();

    }

    @Override
    public void removeItemFromShoppingCart(RemoveItemFromShoppingCartRequest request, StreamObserver<RemoveItemFromShoppingCartResponse> responseObserver) {
        RemoveItemFromShoppingCartResponse.Builder builder = RemoveItemFromShoppingCartResponse.newBuilder();
        Optional<ShoppingCartItem> shoppingCartItemOptional = this.shoppingCartItemRepository
                .findById(request.getCartItemId());
        if(shoppingCartItemOptional.isPresent()){
            this.shoppingCartItemRepository.deleteById(request.getCartItemId());
            builder.setSuccess(true);
            responseObserver.onNext(builder.build());
        }
        else {
            Status status = Status.NOT_FOUND.withDescription("The shopping cart item is not found");
            responseObserver.onError(status.asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void checkoutCart(CheckoutCartRequest request, StreamObserver<CheckoutCartResponse> responseObserver) {
        CheckoutCartResponse.Builder builder = CheckoutCartResponse.newBuilder();
        Optional<ShoppingCart> optionalShoppingCart = this.shoppingCartRepository.findById(request.getCartId());
        User user = AuthConstant.AUTHORIZED_USER.get();
        if(optionalShoppingCart.isPresent()){
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            Status status;
            if(shoppingCart.getStatus().equals(ShoppingCartStatus.EXPIRED)){
                status = Status.INVALID_ARGUMENT.withDescription("The shopping cart has expired");
                responseObserver.onError(status.asRuntimeException());
            }
            else if(shoppingCart.getStatus().equals(ShoppingCartStatus.CHECKOUT)){
                status = Status.INVALID_ARGUMENT.withDescription("The shopping cart has already been checked out");
                responseObserver.onError(status.asRuntimeException());
            }
            else {
                List<OrderItem> orderItemList = new ArrayList<>();
                BigDecimal grandTotal = new BigDecimal(0);
                BigDecimal grandDiscountAmount = new BigDecimal(0);
                BigDecimal grandFinalAmount = new BigDecimal(0);
                List<Product> productUpdates = new ArrayList<>();
                for(ShoppingCartItem shoppingCartItem: shoppingCart.getShoppingCartItems()){
                    OrderItem item = new OrderItem();
                    Product product = shoppingCartItem.getProduct();
                    BigDecimal price = product.getProductPrice().getPrice();
                    int quantity = shoppingCartItem.getQuantity();

                    //Check if the product quantity is sufficient
                    int existingProductQuantity = product.getProductInventory().getQuantity();
                    if(quantity > existingProductQuantity ){
                        Status errorStatus = Status.FAILED_PRECONDITION
                                .withDescription("No enough quantity in stock");
                        responseObserver.onError(errorStatus.asRuntimeException());
                    }
                    // Deduct product quantity
                    product.getProductInventory().setQuantity(existingProductQuantity- quantity);
                    productUpdates.add(product);

                    BigDecimal discountInAmount = new BigDecimal(0);
                    // Calculate discount
                    if(shoppingCartItem.getDiscount() != null){
                        Discount discount = shoppingCartItem.getDiscount();
                        // Check if the discount is active
                        if(discount.isActive()) {
                            BigDecimal discountValue = discount.getDiscountValue();
                            DiscountType discountType = discount.getDiscountType();
                            if (discountType.equals(DiscountType.AMOUNT)) {
                                discountInAmount = discountValue.multiply(new BigDecimal(quantity));
                                item.setDiscountAmount(discountInAmount);
                            }
                            if (discount.getDiscountType().equals(DiscountType.PERCENT)) {
                                BigDecimal discountAmount = price.multiply(discountValue.divide(new BigDecimal(100)));
                                discountInAmount = discountAmount.multiply(new BigDecimal(quantity));
                                item.setDiscountAmount(this.priceUtil.sanitizePrice(discountInAmount));
                            }
                            grandDiscountAmount = grandDiscountAmount.add(discountInAmount);
                            item.setDiscount(shoppingCartItem.getDiscount());
                        }
                    }


                    BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));
                    BigDecimal finaAmount = totalAmount.subtract(discountInAmount);
                    grandFinalAmount =grandFinalAmount.add(finaAmount);
                    grandTotal = grandTotal.add(totalAmount);
                    item.setProduct(product);
                    item.setPrice(price);
                    item.setQuantity(quantity);
                    item.setTotalAmount(this.priceUtil.sanitizePrice(totalAmount));
                    item.setFinalAmount(this.priceUtil.sanitizePrice(finaAmount));
                    orderItemList.add(item);
                }
                Order order = new Order();
                order.setOrderItemList(orderItemList);
                order.setGrandTotal(this.priceUtil.sanitizePrice(grandTotal));
                order.setDiscountAmount(this.priceUtil.sanitizePrice(grandDiscountAmount));
                order.setFinalAmount(this.priceUtil.sanitizePrice(grandFinalAmount));
                order.setStatus(OrderStatus.CREATED);
                this.productRepository.saveAll(productUpdates);
                List<Order> orders = user.getOrders();
                if(orders == null){
                    orders = new ArrayList<>();
                }
                orders.add(order);
                user.setOrders(orders);
                User savedUser = this.userRepository.save(user);
                List<Order> savedOrders = savedUser.getOrders();
                Order savedOrder = savedOrders.get(savedOrders.size() -1 );
                builder.setOrder(this.orderResponseMapper.mapResponse(savedOrder));
                shoppingCart.setStatus(ShoppingCartStatus.CHECKOUT);
                this.shoppingCartRepository.save(shoppingCart);
                responseObserver.onNext(builder.build());
            }

        }
        else{
            Status status = Status.NOT_FOUND.withDescription("The shopping cart is not found");
            responseObserver.onError(status.asRuntimeException());
        }
        responseObserver.onCompleted();

    }

    @Override
    public void viewOrders(ViewOrdersRequest request, StreamObserver<ViewOrdersResponse> responseObserver) {
        ViewOrdersResponse.Builder builder = ViewOrdersResponse.newBuilder();
        User user = AuthConstant.AUTHORIZED_USER.get();
        List<Order> orders = user.getOrders();
        builder.addAllOrders(orders.stream().map(order -> this.orderResponseMapper.mapResponse(order)).collect(Collectors.toList()));
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void cancelOrder(CancelOrderRequest request, StreamObserver<CancelOrderResponse> responseObserver) {
        CancelOrderResponse.Builder builder = CancelOrderResponse.newBuilder();
        Optional<Order> optionalOrder = this.orderRepository.findById(request.getOrderId());
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            OrderStatus orderStatus = order.getStatus();
            if(orderStatus.equals(OrderStatus.FULFILLED) || orderStatus.equals(OrderStatus.CANCELLED)){
                Status status = Status.NOT_FOUND.withDescription("The order cannot be cancelled");
                responseObserver.onError(status.asRuntimeException());
            }
            else {
                CancelOrder cancelOrder = new CancelOrder();
                cancelOrder.setReason(request.getMessage());
                cancelOrder.setOrder(order);
                this.cancelledOrderRepository.save(cancelOrder);
                builder.setSuccess(true);
                responseObserver.onNext(builder.build());
            }
        }
        else{
            Status status = Status.NOT_FOUND.withDescription("The order is not found");
            responseObserver.onError(status.asRuntimeException());
        }
        responseObserver.onCompleted();
    }
}
