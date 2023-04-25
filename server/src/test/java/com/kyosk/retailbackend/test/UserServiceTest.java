package com.kyosk.retailbackend.test;

import com.google.protobuf.Timestamp;
import com.kyosk.retailbackend.AuthorizeUserRequest;
import com.kyosk.retailbackend.AuthorizeUserResponse;
import com.kyosk.retailbackend.CreateUserRequest;
import com.kyosk.retailbackend.CreateUserResponse;
import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.repository.UserRepository;
import com.kyosk.retailbackend.service.UserService;
import com.kyosk.retailbackend.utils.BcryptGenerator;
import com.kyosk.retailbackend.utils.JwtUtil;
import com.kyosk.retailbackend.utils.TimeUtils;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;

    private BcryptGenerator bcryptGenerator;

    private TimeUtils timeUtils;

    private JwtUtil jwtUtil;

    private UserService userService;

    @BeforeEach
    public void setupService(){
        userRepository = mock(UserRepository.class);
        bcryptGenerator = mock(BcryptGenerator.class);
        timeUtils = mock(TimeUtils.class);
        jwtUtil = mock(JwtUtil.class);
        userService = new UserService(userRepository, bcryptGenerator, timeUtils,jwtUtil);
    }

    @Test
    public  void createUserFailsWhenEmailExistsTest() throws Exception {
        User user = new User(
                "Cretus",
                "Mukiza",
                "cretusmukiza@gmail.com",
                "+255757807834", "password");

        when(userRepository.findByEmail("cretusmukiza@gmail.com")).thenReturn(Optional.of(user));

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setFirstName("Cretus")
                .setLastName("Mukiza")
                .setEmail("cretusmukiza@gmail.com")
                .setPhoneNumber("+255757807834")
                .setPassword("password").build();

        StreamRecorder<CreateUserResponse> responseObserver = StreamRecorder.create();
        this.userService.createUser(request, responseObserver);

        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            fail("The test did not terminate in time");
        }

        assertNotNull(responseObserver.getError());

    }

    @Test
    public  void createUserWithSuccess() throws Exception {
        User user = new User(
                "Cretus",
                "Mukiza",
                "cretusmukiza@gmail.com",
                "+255757807834", "password");
        LocalDateTime localDateTime = LocalDateTime.now();
        User userResponse = user;
        userResponse.setUpdatedAt(localDateTime);
        userResponse.setCreatedAt(localDateTime);
        userResponse.setId(Long.valueOf(1));

        when(userRepository.findByEmail("cretusmukiza@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(userResponse);
        when(bcryptGenerator.encodePassword("password")).thenReturn("password");
        when(timeUtils.toGoogleTimestamp(Mockito.any())).thenReturn(Timestamp.newBuilder().build());

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setFirstName("Cretus")
                .setLastName("Mukiza")
                .setEmail("cretusmukiza@gmail.com")
                .setPhoneNumber("+255757807834")
                .setPassword("password").build();


        StreamRecorder<CreateUserResponse> responseObserver = StreamRecorder.create();
        this.userService.createUser(request, responseObserver);

        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            fail("The test did not terminate in time");
        }

        assertNull(responseObserver.getError());
    }

    @Test
    public void authorizeUserFailsWhenUserDoesNotExist() throws Exception {
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        AuthorizeUserRequest request =  AuthorizeUserRequest.newBuilder()
               .setEmail("email@gmail.com")
               .setPassword("password")
               .build();
        StreamRecorder<AuthorizeUserResponse> responseObserver = StreamRecorder.create();
        this.userService.authorizeUser(request, responseObserver);
        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            fail("The test did not terminate in time");
        }
        assertNotNull(responseObserver.getError());

    }

    @Test
    public void authorizeUserFailsWhenCredentialDoNotMatch() throws Exception {
        User user = new User(
                "Cretus",
                "Mukiza",
                "cretusmukiza@gmail.com",
                "+255757807834", "password");
        LocalDateTime localDateTime = LocalDateTime.now();
        User userResponse = user;
        userResponse.setUpdatedAt(localDateTime);
        userResponse.setCreatedAt(localDateTime);
        userResponse.setId(Long.valueOf(1));
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(userResponse));
        when(bcryptGenerator.isValidPassword(Mockito.any(),Mockito.any())).thenReturn(false);
        AuthorizeUserRequest request =  AuthorizeUserRequest.newBuilder()
                .setEmail("email@gmail.com")
                .setPassword("password")
                .build();
        StreamRecorder<AuthorizeUserResponse> responseObserver = StreamRecorder.create();
        this.userService.authorizeUser(request, responseObserver);
        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            fail("The test did not terminate in time");
        }
        assertNotNull(responseObserver.getError());
    }


    @Test
    public void authorizeUserWithSuccess() throws Exception {
        User user = new User(
                "Cretus",
                "Mukiza",
                "cretusmukiza@gmail.com",
                "+255757807834", "password");
        LocalDateTime localDateTime = LocalDateTime.now();
        User userResponse = user;
        userResponse.setUpdatedAt(localDateTime);
        userResponse.setCreatedAt(localDateTime);
        userResponse.setId(Long.valueOf(1));
        when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(userResponse));
        when(bcryptGenerator.isValidPassword(Mockito.any(),Mockito.any())).thenReturn(true);
        when(jwtUtil.generateAccessToken(Mockito.any())).thenReturn("token");
        AuthorizeUserRequest request =  AuthorizeUserRequest.newBuilder()
                .setEmail("email@gmail.com")
                .setPassword("password")
                .build();
        StreamRecorder<AuthorizeUserResponse> responseObserver = StreamRecorder.create();
        this.userService.authorizeUser(request, responseObserver);
        if(!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)){
            fail("The test did not terminate in time");
        }
        assertNull(responseObserver.getError());
    }

}
