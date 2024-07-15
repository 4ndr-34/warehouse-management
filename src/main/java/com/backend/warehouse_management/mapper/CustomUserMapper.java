package com.backend.warehouse_management.mapper;


import com.backend.warehouse_management.dto.client.UserRegisterRequest;
import com.backend.warehouse_management.entity.User;
import com.backend.warehouse_management.enums.UserRole;

public class CustomUserMapper {

    public static User registerRequestToUser(UserRegisterRequest registerRequest) {
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(UserRole.CLIENT);
        return user;
    }




}
