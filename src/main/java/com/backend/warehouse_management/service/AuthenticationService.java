package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.AuthenticationRequest;
import com.backend.warehouse_management.dto.AuthenticationResponse;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.dto.client.UserRegisterRequest;

public interface AuthenticationService {

    UserCRUDResponse register(UserRegisterRequest registerRequest);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

}
