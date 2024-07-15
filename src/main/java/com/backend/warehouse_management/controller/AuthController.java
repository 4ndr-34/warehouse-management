package com.backend.warehouse_management.controller;

import com.backend.warehouse_management.dto.AuthenticationRequest;
import com.backend.warehouse_management.dto.AuthenticationResponse;
import com.backend.warehouse_management.dto.admin.UserCRUDResponse;
import com.backend.warehouse_management.dto.client.UserRegisterRequest;
import com.backend.warehouse_management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    public final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserCRUDResponse> register(@RequestBody UserRegisterRequest registerRequest) {
        return new ResponseEntity(authenticationService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.login(authenticationRequest), HttpStatus.OK);
    }

}
