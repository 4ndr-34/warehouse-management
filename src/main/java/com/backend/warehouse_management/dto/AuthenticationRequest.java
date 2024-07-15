package com.backend.warehouse_management.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {

    public String email;
    public String password;
}
