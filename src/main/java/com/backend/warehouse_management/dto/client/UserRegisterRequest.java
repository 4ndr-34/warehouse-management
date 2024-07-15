package com.backend.warehouse_management.dto.client;


import lombok.Data;

@Data
public class UserRegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
