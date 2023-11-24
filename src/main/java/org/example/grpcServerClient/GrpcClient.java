package org.example.grpcServerClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.UserOuterClass;
import org.example.UserServiceGrpc;

public class GrpcClient {
    public static void main(String[] args) {
        // Create a channel to the gRPC server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8085).usePlaintext().build();

        // Create a blocking stub for the UserService
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(channel);

        // Create a UserRequest
        UserOuterClass.UserRequest request = UserOuterClass.UserRequest.newBuilder().setId(10)
                .build();

        // Make a gRPC call to GetUser
        UserOuterClass.User user = blockingStub.deleteUser(request);

        // Print the result
        System.out.println("Received user: " + user);

        // Shutdown the channel
        channel.shutdown();
    }
}
