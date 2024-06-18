package com.backend.warehouse_management.dto.admin;


import com.backend.warehouse_management.enums.UserRole;
import lombok.Data;

@Data
public class UserCRUDRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;

}
