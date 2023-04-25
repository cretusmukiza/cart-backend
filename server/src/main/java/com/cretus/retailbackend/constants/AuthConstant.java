package com.cretus.retailbackend.constants;

import com.cretus.retailbackend.entity.User;
import io.grpc.Context;
import io.grpc.Metadata;

public class AuthConstant {
    public static final Metadata.Key<String> TOKEN = Metadata.Key
            .of("client-token", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<User> AUTHORIZED_USER = Context.key("user");
}
