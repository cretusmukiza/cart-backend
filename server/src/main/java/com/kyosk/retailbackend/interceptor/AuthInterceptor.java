package com.kyosk.retailbackend.interceptor;

import com.kyosk.retailbackend.constants.AuthConstant;
import com.kyosk.retailbackend.entity.User;
import com.kyosk.retailbackend.utils.JwtUtil;
import io.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthInterceptor implements ServerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler)
     {
        String clientToken = metadata.get(AuthConstant.TOKEN);
         try{
             User user = this.jwtUtil.getUserFromToken(clientToken);
             Context context = Context.current().withValue(
                     AuthConstant.AUTHORIZED_USER,
                     user
             );
             //serverCallHandler.startCall(serverCall, metadata);
             return Contexts.interceptCall(context,serverCall, metadata, serverCallHandler);
         }
         catch(Exception exception) {
             Status status = Status.UNAUTHENTICATED.withDescription(exception.getMessage());
             serverCall.close(status, metadata);
         }
        return new ServerCall.Listener<ReqT>() {};

    }
}
