package org.example.service;

import com.zaxxer.hikari.HikariDataSource;
import io.grpc.stub.StreamObserver;
import org.example.UserOuterClass;
import org.example.UserServiceGrpc;
import org.example.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class MyServerImpl extends UserServiceGrpc.UserServiceImplBase {



    private final HikariDataSource dataSource;

    @Autowired
    public MyServerImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void getUser(UserOuterClass.UserRequest request, StreamObserver<UserOuterClass.User> responseObserver) {
       int userId= request.getId();
       getUser(userId);

    }

    @Transactional
    @Override
    public void createUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> responseObserver) {
        super.createUser(request, responseObserver);
        try {
            UserEntity user = toUserEntity(request);
            saveIntoDatabase(user);
            responseObserver.onNext(request);
            responseObserver.onCompleted();
        }
        catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("Error while processing :"+e);
        }
    }

    @Override
    public void updateUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> responseObserver) {
        super.updateUser(request, responseObserver);
        int userId = request.getId();
         UserEntity userEntity=toUserEntity(request);
         updateUser(userEntity,userId);
        responseObserver.onNext(request);
        responseObserver.onCompleted();

    }

    @Override
    public void deleteUser(UserOuterClass.UserRequest request, StreamObserver<UserOuterClass.User> responseObserver) {
        super.deleteUser(request, responseObserver);
        int userId = request.getId();
        deleteUser(userId);
      responseObserver.onCompleted();

    }
    private UserEntity toUserEntity(UserOuterClass.User user) {
        return new UserEntity(user.getId(), user.getName(), user.getRoll());
    }
    private UserOuterClass.User mapUserEntityToUser(UserEntity userEntity) {
        return UserOuterClass.User.newBuilder()
                .setId(userEntity.getId())
                .setName(userEntity.getName())
                .setRoll(userEntity.getRoll())
                .build();
    }
    private void saveIntoDatabase(UserEntity userEntity){
        try(Connection connection= dataSource.getConnection()){
            String query="INSERT INTO user_entity(id,name,roll) VALUES (?,?,?)";
            try(PreparedStatement statement= connection.prepareStatement(query)){
                statement.setInt(1,userEntity.getId());
                statement.setString(2, userEntity.getName());
                statement.setString(3, userEntity.getRoll());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void getUser(int id) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM user_entity WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String roll = resultSet.getString("roll");

                        new UserEntity(userId, name, roll);
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteUser(int id){

        try(Connection connection= dataSource.getConnection()) {
            String query="DELETE FROM user_entity WHERE id=?";
            try(PreparedStatement statement= connection.prepareStatement(query)){
                statement.setInt(1,id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 0) {
                    System.out.println("No user with ID " + id + " found for deletion.");
                } else {
                    System.out.println("User with ID " + id + " deleted successfully.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateUser(UserEntity userEntity,int id){
        try(Connection connection= dataSource.getConnection()) {
            String query="UPDATE user_entity SET name=?,roll=? WHERE id=?";
            try(PreparedStatement statement= connection.prepareStatement(query)){
                statement.setString(1, userEntity.getName());
                statement.setString(2, userEntity.getRoll());
                statement.setInt(3,id);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 0) {
                    System.out.println("No user with ID " + userEntity.getId() + " found for update.");
                } else {
                    System.out.println("User with ID " + userEntity.getId() + " updated successfully.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
