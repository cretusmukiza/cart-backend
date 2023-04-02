package com.kyosk.retailbackend.service;

import com.kyosk.retailbackend.*;
import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.repository.UserRepository;
import com.kyosk.retailbackend.utils.BcryptGenerator;
import com.kyosk.retailbackend.utils.JwtUtil;
import com.kyosk.retailbackend.utils.TimeUtils;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BcryptGenerator bcryptGenerator;

    @Autowired
    private TimeUtils timeUtils;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        CreateUserResponse.Builder builder = CreateUserResponse.newBuilder();
        Optional<User> userExist = this.userRepository.findByEmail(request.getEmail());
        if(userExist.isPresent()){
            Status status =  Status.ALREADY_EXISTS.withDescription("User already exists");
            responseObserver.onError(status.asRuntimeException());
            return;
        }
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                this.bcryptGenerator.encodePassword(request.getPassword())
        );

        User savedUser = this.userRepository.save(user);

        builder.setId(savedUser.getId())
                .setFirstName(savedUser.getFirstName())
                .setLastName(savedUser.getLastName())
                .setEmail(savedUser.getEmail())
                .setPhoneNumber(savedUser.getPhoneNumber())
                .setCreatedAt(timeUtils.toGoogleTimestamp(savedUser.getCreatedAt()))
                .setUpdatedAt(timeUtils.toGoogleTimestamp(savedUser.getUpdatedAt()));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void authorizeUser(AuthorizeUserRequest request, StreamObserver<AuthorizeUserResponse> responseObserver) {
        
    }
}
