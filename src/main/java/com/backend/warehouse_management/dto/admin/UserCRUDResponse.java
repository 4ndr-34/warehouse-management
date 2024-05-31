package com.backend.warehouse_management.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCRUDResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String role;

}
