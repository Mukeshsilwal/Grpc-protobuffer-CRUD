package org.example.config;

import com.zaxxer.hikari.HikariDataSource;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.service.MyServerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Configuration
public class GrpcConfig {
     private final HikariDataSource dataSource;

    public GrpcConfig(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Bean
    public Server grpcServer(MyServerImpl server) throws IOException {

       int port=8085;
       Server server1= ServerBuilder.forPort(port).addService(server).build();
       server1.start();
       return server1;
    }
    @Bean
    public void tableCreator(){
        try(Connection connection=dataSource.getConnection()) {
            String query="CREATE TABLE IF NOT EXISTS user_entity ("
                    + "id INT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "department VARCHAR(255)"
                    + ")";
            try(PreparedStatement statement= connection.prepareStatement(query)){
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
